package cz.jbenes.idc.tableexporter.exporter;

import java.util.List;

import cz.jbenes.idc.tableexporter.exporter.model.PreparedTable;

/**
 * Exporter interface defines the contract for exporting prepared tables.
 * Implementations of this interface will handle the actual export logic.
 */
public interface Exporter {
    
    /**
     * Exports a list of prepared tables to the specified directory.
     *
     * @param tables    List of prepared tables to export
     * @param directory Directory where the exported files will be saved
     */
    void export(List<PreparedTable> tables, String directory);
}
