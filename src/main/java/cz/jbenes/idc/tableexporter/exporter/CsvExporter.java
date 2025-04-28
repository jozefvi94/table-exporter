package cz.jbenes.idc.tableexporter.exporter;

import java.util.List;

import cz.jbenes.idc.tableexporter.exporter.model.PreparedTable;

public class CsvExporter implements Exporter {

    @Override
    public void export(List<PreparedTable> tables, String directory) {
        throw new UnsupportedOperationException("CSV export not implemented yet.");
    }

}
