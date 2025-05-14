# Table Exporter

## How to use

1. Run `mvn clean package` to build the project.
2. Run `java -jar target/tableexporter-0.1.0-SNAPSHOT.jar` to start the application.

## Main components

Program is divided into several components.

### TableDataLoader

Loads data from CSV file and converts it to `RawDataRow`s.

### TableDataProcessor

Processes loaded data and allows for requested operatons on it, e.g. filtering, sorting, etc.

### ExportDataProcessor

Takes processed data and prepares them in requested format for export.

### Exporter

Interface, that takes on input prepared table data and exports it to the desired format, e.g. HTML, Excel, etc.
