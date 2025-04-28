package cz.jbenes.idc.tableexporter.exporter.model;

import java.util.List;

/**
 * PreparedTable represents a table of prepared data for export.
 * It contains the country, quarter, rows of data, and total units.
 */
public class PreparedTable {
    private String country;
    private String quarter;
    private List<PreparedRow> rows;
    private double totalUnits;

    public PreparedTable() {
    }

    public PreparedTable(String country, String quarter, List<PreparedRow> rows, double totalUnits) {
        this.country = country;
        this.quarter = quarter;
        this.rows = rows;
        this.totalUnits = totalUnits;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public List<PreparedRow> getRows() {
        return rows;
    }

    public void setRows(List<PreparedRow> rows) {
        this.rows = rows;
    }

    public double getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(double totalUnits) {
        this.totalUnits = totalUnits;
    }
}

