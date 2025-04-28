package cz.jbenes.idc.tableexporter.processor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.jbenes.idc.tableexporter.exporter.model.PreparedRow;
import cz.jbenes.idc.tableexporter.exporter.model.PreparedTable;
import cz.jbenes.idc.tableexporter.loader.model.RawRowData;

/**
 * ExportDataProcessor processes raw data and prepares it for export.
 * It calculates the share of units sold by each vendor in a given country and quarter.
 */
public class ExportDataProcessor {
    
    private TableDataProcessor tableDataProcessor;

    public ExportDataProcessor(TableDataProcessor tableDataProcessor) {
        this.tableDataProcessor = tableDataProcessor;
    }

    /**
     * Prepares a table for a specific country and quarter.
     * It filters the raw data for the given country and quarter,
     * sorts the vendors by units sold, and calculates the share of each vendor.
     * It also adds an "Others" row for the remaining vendors if any.
     * @param country
     * @param quarter
     * @return PreparedTable object containing the country, quarter, rows of data, and total units.
     */
    public PreparedTable prepareTable(String country, String quarter) {
        // Get all rows for the given country and quarter
        List<RawRowData> vendorData = tableDataProcessor.getRows().stream()
                .filter(row -> row.getCountry().equalsIgnoreCase(country) && row.getQuarter().equalsIgnoreCase(quarter))
                .collect(Collectors.toList());

        // Sort rows by units sold in descending order
        vendorData.sort(Comparator.comparingDouble(RawRowData::getUnits).reversed());

        // Calculate the total units for the country and quarter
        double totalUnits = tableDataProcessor.getTotalUnits(country, quarter);

        // Create the prepared rows with computed share
        List<PreparedRow> preparedRows = vendorData.stream()
            .limit(3) // Pick only the top 3 vendors
            .map(row -> {
                double share = (row.getUnits() / totalUnits) * 100;
                return new PreparedRow(row.getVendor(), row.getUnits(), share);
            })
            .collect(Collectors.toList());

        // Add "Other" row for the remaining vendors if any
        double otherUnits = vendorData.stream()
                .skip(3) // Skip the top 3 vendors
                .mapToDouble(RawRowData::getUnits)
                .sum();

        if (otherUnits > 0) {
            double otherShare = (otherUnits / totalUnits) * 100;
            preparedRows.add(new PreparedRow("Others", otherUnits, otherShare));
        }

        return new PreparedTable(country, quarter, preparedRows, totalUnits);
    }

    /**
     * Prepares all tables for each country and quarter.
     * It groups the raw data by country and quarter, and prepares a table for each group.
     * @return Map of country to quarter to PreparedTable
     */
    public Map<String, Map<String, PreparedTable>> prepareAllTables() {
        Map<String, Map<String, PreparedTable>> allTables = new HashMap<>();

        // Group rows by country and quarter
        tableDataProcessor.getRows().stream()
            .collect(Collectors.groupingBy(RawRowData::getCountry, 
                    Collectors.groupingBy(RawRowData::getQuarter)))
            .forEach((country, quarterMap) -> {
                Map<String, PreparedTable> countryTables = allTables.computeIfAbsent(country, k -> new HashMap<>());
                quarterMap.forEach((quarter, rows) -> {
                    countryTables.put(quarter, prepareTable(country, quarter));
                });
            });

        return allTables;
    }

    /**
     * Prepares all tables and returns them as a flat list.
     * @return List of PreparedTable
     */
    public List<PreparedTable> prepareAllTablesList() {
        return prepareAllTables().values().stream()
            .flatMap(countryTables -> countryTables.values().stream())
            .collect(Collectors.toList());
    }
}
