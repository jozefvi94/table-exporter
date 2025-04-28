package cz.jbenes.idc.tableexporter.exporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cz.jbenes.idc.tableexporter.exporter.model.PreparedRow;
import cz.jbenes.idc.tableexporter.exporter.model.PreparedTable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HtmlExporterTest {

    private HtmlExporter htmlExporter;
    private String testDirectory;

    @BeforeEach
    public void setUp() {
        htmlExporter = new HtmlExporter();
        testDirectory = "test-output";
        File dir = new File(testDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void testExport_createsHtmlFile() throws IOException {
        List<PreparedTable> tables = new ArrayList<>();
        PreparedTable table = new PreparedTable();
        table.setCountry("USA");
        table.setQuarter("Q1");
        table.setTotalUnits(1000);

        List<PreparedRow> rows = new ArrayList<>();
        PreparedRow row1 = new PreparedRow();
        row1.setVendor("Vendor A");
        row1.setUnits(600);
        row1.setShare(60.0);
        rows.add(row1);

        PreparedRow row2 = new PreparedRow();
        row2.setVendor("Vendor B");
        row2.setUnits(400);
        row2.setShare(40.0);
        rows.add(row2);

        table.setRows(rows);
        tables.add(table);

        htmlExporter.export(tables, testDirectory);

        File outputFile = new File(testDirectory + "/USA_Q1.html");
        assertTrue(outputFile.exists(), "HTML file should be created");
        String content = Files.readString(outputFile.toPath());
        assertTrue(content.contains("<h1>USA Q1 Market Share</h1>"), "HTML content should contain the correct header");
        assertTrue(content.contains("<td>Vendor A</td>"), "HTML content should contain Vendor A");
        assertTrue(content.contains("<td>Vendor B</td>"), "HTML content should contain Vendor B");
        assertTrue(content.contains("<b>100%</b>"), "HTML content should contain total share of 100%");
    }
}