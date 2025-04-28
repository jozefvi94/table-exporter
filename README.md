# Table Exporter

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
