package cz.jbenes.idc.tableexporter.exporter.model;

/**
 * PreparedRow represents a row in the prepared table for export.
 */
public class PreparedRow {
    private String vendor;
    private double units;
    private double share;

    public PreparedRow() {
    }

    public PreparedRow(String vendor, double units, double share) {
        this.vendor = vendor;
        this.units = units;
        this.share = share;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getUnits() {
        return units;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public double getShare() {
        return share;
    }

    public void setShare(double share) {
        this.share = share;
    }
}

