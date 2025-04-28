package cz.jbenes.idc.tableexporter;
import java.util.List;

import cz.jbenes.idc.tableexporter.exporter.HtmlExporter;
import cz.jbenes.idc.tableexporter.exporter.model.PreparedTable;
import cz.jbenes.idc.tableexporter.loader.TableDataLoader;
import cz.jbenes.idc.tableexporter.loader.model.RawRowData;
import cz.jbenes.idc.tableexporter.processor.ExportDataProcessor;
import cz.jbenes.idc.tableexporter.processor.TableDataProcessor;

public class Main {

    public static void main(String[] args) {
        TableDataLoader dataLoader = new TableDataLoader();
        List<RawRowData> rawData = dataLoader.loadCSV("src/main/resources/data.csv");
        TableDataProcessor tableData = new TableDataProcessor(rawData);

        ExportDataProcessor exportDataProcessor = new ExportDataProcessor(tableData);
        List<PreparedTable> preparedTables = exportDataProcessor.prepareAllTablesList();

        HtmlExporter htmlExporter = new HtmlExporter();
        htmlExporter.export(preparedTables, "output");
    }
}
