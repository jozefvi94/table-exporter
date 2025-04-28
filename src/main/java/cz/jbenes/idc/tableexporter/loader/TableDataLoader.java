package cz.jbenes.idc.tableexporter.loader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import cz.jbenes.idc.tableexporter.loader.model.RawRowData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TableDataLoader is responsible for loading raw data from a CSV file.
 * It reads the file, parses the data, and returns a list of RawRowData objects.
 */
public class TableDataLoader {

    /**
     * Loads raw data from a CSV file.
     *
     * @param filePath Path to the CSV file
     * @return List of RawRowData objects containing the loaded data
     */
    public List<RawRowData> loadCSV(String filePath) {
        List<RawRowData> rowDataList = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {
            String[] values;
            csvReader.readNext(); // Skip the header row
            while ((values = csvReader.readNext()) != null) {
                rowDataList.add(new RawRowData(values[0], values[1], values[2], Double.parseDouble(values[3])));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (IOException e) {
            System.err.println("An I/O error occurred while reading the file: " + filePath + ". Error: " + e.getMessage());
        } catch (CsvValidationException e) {
            System.err.println("CSV validation error occurred while reading the file: " + filePath + ". Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Number format error in file: " + filePath + ". Ensure numeric values are correctly formatted. Error: " + e.getMessage());
        }
        
        return rowDataList;
    }
}
