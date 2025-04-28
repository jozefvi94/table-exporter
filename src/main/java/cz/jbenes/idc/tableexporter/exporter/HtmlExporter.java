package cz.jbenes.idc.tableexporter.exporter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import cz.jbenes.idc.tableexporter.exporter.model.PreparedRow;
import cz.jbenes.idc.tableexporter.exporter.model.PreparedTable;

/**
 * HtmlExporter is an implementation of the Exporter interface that exports prepared tables to HTML files.
 * Each table is saved as a separate HTML file in the specified directory.
 */
public class HtmlExporter implements Exporter {

    @Override
    public void export(List<PreparedTable> tables, String directory) {
        Path outputDir = Paths.get(directory);
        if (!outputDir.toFile().exists()) {
            outputDir.toFile().mkdirs();
        }

        for (PreparedTable table : tables) {
            String filePath = directory + "/" + table.getCountry() + "_" + table.getQuarter() + ".html";

            StringBuilder htmlBuilder = new StringBuilder();
            htmlBuilder.append("<html><body>");
            htmlBuilder.append("<h1>").append(table.getCountry()).append(" ").append(table.getQuarter()).append(" Market Share</h1>");
            htmlBuilder.append("<table border='1'><thead><tr><th>Vendor</th><th>Units</th><th>Share</th></tr></thead><tbody>");

            for (PreparedRow row : table.getRows()) {
                htmlBuilder.append("<tr><td>").append(row.getVendor()).append("</td><td>")
                           .append(String.format("%,.0f", row.getUnits())).append("</td><td>")
                           .append(String.format("%.1f", row.getShare())).append("%</td></tr>");
            }

            htmlBuilder.append("<tr><td><b>Total</b></td><td><b>")
                       .append(String.format("%,.0f",table.getTotalUnits())).append("</b></td><td><b>100%</b></td></tr>");
            htmlBuilder.append("</tbody></table>");
            htmlBuilder.append("</body></html>");

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(htmlBuilder.toString());
            } catch (IOException e) {
                System.err.println("Failed to export table for " + table.getCountry() + " " + table.getQuarter() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
 }
