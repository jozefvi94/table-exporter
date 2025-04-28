package cz.jbenes.idc.tableexporter.processor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cz.jbenes.idc.tableexporter.exporter.model.PreparedRow;
import cz.jbenes.idc.tableexporter.exporter.model.PreparedTable;
import cz.jbenes.idc.tableexporter.loader.model.RawRowData;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

class ExportDataProcessorTest {

    private TableDataProcessor mockTableDataProcessor;
    private ExportDataProcessor exportDataProcessor;

    @BeforeEach
    void setUp() {
        mockTableDataProcessor = mock(TableDataProcessor.class);
        exportDataProcessor = new ExportDataProcessor(mockTableDataProcessor);
    }

    @Test
    void testPrepareTable() {
        String country = "USA";
        String quarter = "Q1";
        List<RawRowData> mockRows = Arrays.asList(
            new RawRowData(country, quarter, "VendorA", 500),
            new RawRowData(country, quarter, "VendorB", 300),
            new RawRowData(country, quarter, "VendorC", 200),
            new RawRowData(country, quarter, "VendorD", 100)
        );
        when(mockTableDataProcessor.getRows()).thenReturn(mockRows);
        when(mockTableDataProcessor.getTotalUnits(country, quarter)).thenReturn(1100.0);

        PreparedTable preparedTable = exportDataProcessor.prepareTable(country, quarter);

        assertEquals(country, preparedTable.getCountry());
        assertEquals(quarter, preparedTable.getQuarter());
        assertEquals(4, preparedTable.getRows().size());

        PreparedRow row1 = preparedTable.getRows().get(0);
        assertEquals("VendorA", row1.getVendor());
        assertEquals(500, row1.getUnits());
        assertEquals(45.45, row1.getShare(), 0.01);

        PreparedRow row4 = preparedTable.getRows().get(3);
        assertEquals("Others", row4.getVendor());
        assertEquals(100, row4.getUnits());
        assertEquals(9.09, row4.getShare(), 0.01);
    }

    @Test
    void testPrepareAllTables() {
        List<RawRowData> mockRows = Arrays.asList(
            new RawRowData("USA", "Q1", "VendorA", 500),
            new RawRowData("USA", "Q1", "VendorB", 300),
            new RawRowData("Canada", "Q2", "VendorC", 200)
        );
        when(mockTableDataProcessor.getRows()).thenReturn(mockRows);
        when(mockTableDataProcessor.getTotalUnits("USA", "Q1")).thenReturn(800.0);
        when(mockTableDataProcessor.getTotalUnits("Canada", "Q2")).thenReturn(200.0);

        Map<String, Map<String, PreparedTable>> allTables = exportDataProcessor.prepareAllTables();

        assertEquals(2, allTables.size());
        assertTrue(allTables.containsKey("USA"));
        assertTrue(allTables.containsKey("Canada"));

        PreparedTable usaTable = allTables.get("USA").get("Q1");
        assertNotNull(usaTable);
        assertEquals(2, usaTable.getRows().size());

        PreparedTable canadaTable = allTables.get("Canada").get("Q2");
        assertNotNull(canadaTable);
        assertEquals(1, canadaTable.getRows().size());
    }

    @Test
    void testPrepareAllTablesList() {
        List<RawRowData> mockRows = Arrays.asList(
            new RawRowData("USA", "Q1", "VendorA", 500),
            new RawRowData("USA", "Q1", "VendorB", 300),
            new RawRowData("Canada", "Q2", "VendorC", 200)
        );
        when(mockTableDataProcessor.getRows()).thenReturn(mockRows);
        when(mockTableDataProcessor.getTotalUnits("USA", "Q1")).thenReturn(800.0);
        when(mockTableDataProcessor.getTotalUnits("Canada", "Q2")).thenReturn(200.0);

        List<PreparedTable> preparedTables = exportDataProcessor.prepareAllTablesList();

        assertEquals(2, preparedTables.size());
        assertTrue(preparedTables.stream().anyMatch(table -> 
            "USA".equals(table.getCountry()) && "Q1".equals(table.getQuarter())));
        assertTrue(preparedTables.stream().anyMatch(table -> 
            "Canada".equals(table.getCountry()) && "Q2".equals(table.getQuarter())));
    }
}