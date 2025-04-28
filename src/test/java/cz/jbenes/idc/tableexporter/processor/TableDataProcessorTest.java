package cz.jbenes.idc.tableexporter.processor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cz.jbenes.idc.tableexporter.loader.model.RawRowData;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TableDataProcessorTest {

    private TableDataProcessor processor;

    @BeforeEach
    void setUp() {
        List<RawRowData> rows = Arrays.asList(
            new RawRowData("USA", "Q1", "VendorA", 100),
            new RawRowData("USA", "Q1", "VendorB", 200),
            new RawRowData("USA", "Q2", "VendorA", 150),
            new RawRowData("USA", "Q2", "VendorB", 250),
            new RawRowData("Canada", "Q1", "VendorA", 300),
            new RawRowData("Canada", "Q1", "VendorB", 400)
        );
        processor = new TableDataProcessor(rows);
    }

    @Test
    void testGetTotalUnits() {
        assertEquals(300, processor.getTotalUnits("USA", "Q1"));
        assertEquals(400, processor.getTotalUnits("USA", "Q2"));
        assertEquals(700, processor.getTotalUnits("Canada", "Q1"));
        assertEquals(0, processor.getTotalUnits("Canada", "Q2"));
    }

    @Test
    void testGetVendorShare() {
        assertEquals(33.33, processor.getVendorShare("VendorA", "USA", "Q1"), 0.01);
        assertEquals(66.67, processor.getVendorShare("VendorB", "USA", "Q1"), 0.01);
        assertEquals(37.5, processor.getVendorShare("VendorA", "USA", "Q2"), 0.01);
        assertEquals(62.5, processor.getVendorShare("VendorB", "USA", "Q2"), 0.01);
        assertEquals(42.86, processor.getVendorShare("VendorA", "Canada", "Q1"), 0.01);
        assertEquals(57.14, processor.getVendorShare("VendorB", "Canada", "Q1"), 0.01);
    }

    @Test
    void testGetUnitsAndShareForVendor() {
        String result = processor.getUnitsAndShareForVendor("VendorA", "USA", "Q1");
        assertEquals("VendorA sold 100.00 units and has a 33.33% share in USA, Q1.", result);

        result = processor.getUnitsAndShareForVendor("VendorB", "Canada", "Q1");
        assertEquals("VendorB sold 400.00 units and has a 57.14% share in Canada, Q1.", result);
    }

    @Test
    void testFindRowForVendor() {
        assertEquals(1, processor.findRowForVendor("VendorA", "USA", "Q1"));
        assertEquals(2, processor.findRowForVendor("VendorB", "USA", "Q1"));
        assertEquals(-1, processor.findRowForVendor("VendorC", "USA", "Q1"));
    }

    @Test
    void testSortByVendor() {
        processor.sortByVendor();
        List<RawRowData> sortedRows = processor.getRows();
        assertEquals("VendorA", sortedRows.get(0).getVendor());
        assertEquals("VendorA", sortedRows.get(1).getVendor());
        assertEquals("VendorA", sortedRows.get(2).getVendor());
        assertEquals("VendorB", sortedRows.get(3).getVendor());
        assertEquals("VendorB", sortedRows.get(4).getVendor());
        assertEquals("VendorB", sortedRows.get(5).getVendor());
    }

    @Test
    void testSortByUnits() {
        processor.sortByUnits();
        List<RawRowData> sortedRows = processor.getRows();
        assertEquals(400, sortedRows.get(0).getUnits());
        assertEquals(300, sortedRows.get(1).getUnits());
        assertEquals(250, sortedRows.get(2).getUnits());
        assertEquals(200, sortedRows.get(3).getUnits());
        assertEquals(150, sortedRows.get(4).getUnits());
        assertEquals(100, sortedRows.get(5).getUnits());
    }

}