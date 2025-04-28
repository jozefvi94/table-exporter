package cz.jbenes.idc.tableexporter.processor;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cz.jbenes.idc.tableexporter.loader.model.RawRowData;

/**
 * TableDataProcessor processes raw data and computes totals and shares for each vendor.
 * It provides methods to retrieve total units, vendor shares, and sort the data.
 */
public class TableDataProcessor {
    private List<RawRowData> rows;
    // Maps to store total units and shares for each country and quarter
    private Map<String, Map<String, Double>> totalUnitsMap;
    // Maps to store shares for each vendor in each country and quarter
    private Map<String, Map<String, Map<String, Double>>> shareMap;

    public TableDataProcessor(List<RawRowData> rows) {
        this.rows = rows;
        this.totalUnitsMap = new HashMap<>();
        this.shareMap = new HashMap<>();
        precomputeTotalsAndShares();
    }

    public List<RawRowData> getRows() {
        return rows;
    }

    /**
     * Precomputes total units and shares for each vendor in each country and quarter.
     * This method groups the data by country and quarter, calculates the total units,
     * and computes the share for each vendor.
     */
    private void precomputeTotalsAndShares() {
        // Group rows by country and quarter
        Map<String, Map<String, List<RawRowData>>> groupedData = rows.stream()
                .collect(Collectors.groupingBy(RawRowData::getCountry,
                        Collectors.groupingBy(RawRowData::getQuarter)));

        // Iterate over each country and quarter, calculate total units and shares
        for (String country : groupedData.keySet()) {
            Map<String, List<RawRowData>> quarterlyData = groupedData.get(country);
            for (String quarter : quarterlyData.keySet()) {
                List<RawRowData> vendorData = quarterlyData.get(quarter);
                double totalUnits = vendorData.stream().mapToDouble(RawRowData::getUnits).sum();
                
                // Store total units for the country-quarter pair
                totalUnitsMap.putIfAbsent(country, new HashMap<>());
                totalUnitsMap.get(country).put(quarter, totalUnits);

                // Calculate and store the share for each vendor
                shareMap.putIfAbsent(country, new HashMap<>());
                shareMap.get(country).putIfAbsent(quarter, new HashMap<>());
                for (RawRowData row : vendorData) {
                    double share = (row.getUnits() / totalUnits) * 100;
                    shareMap.get(country).get(quarter).put(row.getVendor(), share);
                }
            }
        }
    }


    /**
     * Get the total units sold for a specific country and quarter.
     * This method retrieves the total units from the precomputed map.
     * @param country
     * @param quarter
     * @return Total units sold for the specified country and quarter.
     *         Returns 0 if the country or quarter is not found.
     */
    public double getTotalUnits(String country, String quarter) {
        return totalUnitsMap.getOrDefault(country, Collections.emptyMap())
                .getOrDefault(quarter, 0.0);
    }

    /**
     * Get the share of a specific vendor in a given country and quarter.
     * This method retrieves the share from the precomputed map.
     * @param vendor
     * @param country
     * @param quarter
     * @return Share of the vendor in the specified country and quarter.
     *         Returns 0 if the vendor, country, or quarter is not found.
     */
    public double getVendorShare(String vendor, String country, String quarter) {
        return shareMap.getOrDefault(country, Collections.emptyMap())
                .getOrDefault(quarter, Collections.emptyMap())
                .getOrDefault(vendor, 0.0);
    }

    /**
     * Get the units sold and share of a specific vendor in a given country and quarter.
     * This method retrieves the units and share from the precomputed data.
     * @param vendor
     * @param country
     * @param quarter
     * @return A string representation of the vendor's units and share in the specified country and quarter.
     */
    public String getUnitsAndShareForVendor(String vendor, String country, String quarter) {
        double units = 0;
        for (RawRowData row : rows) {
            if (row.getVendor().equalsIgnoreCase(vendor) &&
                row.getCountry().equalsIgnoreCase(country) &&
                row.getQuarter().equalsIgnoreCase(quarter)) {
                units = row.getUnits();
                break;
            }
        }
        double share = getVendorShare(vendor, country, quarter);
        return String.format("%s sold %.2f units and has a %.2f%% share in %s, %s.",
                vendor, units, share, country, quarter);
    }

    /**
     * Find the row index for a specific vendor in a given country and quarter.
     * This method returns the 1-based index of the row if found, otherwise -1.
     * @param vendor
     * @param country
     * @param quarter
     * @return The 1-based index of the row for the specified vendor, country, and quarter. 
     *        Returns -1 if the vendor-country-quarter combination is not found.
     */
    public int findRowForVendor(String vendor, String country, String quarter) {
        for (int i = 0; i < rows.size(); i++) {
            RawRowData row = rows.get(i);
            if (row.getVendor().equalsIgnoreCase(vendor) &&
                row.getCountry().equalsIgnoreCase(country) &&
                row.getQuarter().equalsIgnoreCase(quarter)) {
                return i + 1; // 1-based index
            }
        }
        return -1; // Vendor not found
    }

    /**
     * Sort the rows by ascending vendor name.
     */
    public void sortByVendor() {
        rows.sort(Comparator.comparing(RawRowData::getVendor));
    }

    /**
     * Sort the rows by descending units sold.
     */
    public void sortByUnits() {
        rows.sort(Comparator.comparingDouble(RawRowData::getUnits).reversed());
    }

}