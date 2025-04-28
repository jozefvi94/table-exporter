package cz.jbenes.idc.tableexporter.loader.model;

/**
 * RawRowData represents a single row of raw data from the input file.
 * It contains the country, quarter, vendor, and units sold.
 */
public class RawRowData {
    private String country;
    private String quarter;
    private String vendor;
    private double units;

    public RawRowData(String country, String quarter, String vendor, double units) {
        this.country = country;
        this.quarter = quarter;
        this.vendor = vendor;
        this.units = units;
    }

    public String getCountry() {
        return country;
    }

    public String getQuarter() {
        return quarter;
    }

    public String getVendor() {
        return vendor;
    }

    public double getUnits() {
        return units;
    }

    @Override
    public String toString() {
        return vendor + " (" + country + ", " + quarter + "): " + units + " units";
    }
}
