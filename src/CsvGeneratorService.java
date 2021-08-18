import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Primary source for completing this: https://www.baeldung.com/java-csv
/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Abstract class file for a CsvGenerator
 */
public abstract class CsvGeneratorService {
    protected ArrayList<String[]> lines;

    /**
     * No argument constructor; just initializes the lines arraylist
     */
    public CsvGeneratorService() {
        this.lines = new ArrayList<>();
    }

    /**
     * Method to iterate through the Lines field to make the CSV file
     * @return String indicating whether the CSV file was successfully generated
     */
    public abstract String generateCsvFile();

    /**
     * Converts an array of strings to a line in a CSV file
     * @param data data that represents one line of the CSV file
     * @return String representation of the line joined by commas
     */
    protected String convertToCsvLine(String[] data) {
        return Stream.of(data).collect(Collectors.joining(","));
    }

    /**
     * Generates the CSV lines given the provided data
     */
    protected abstract void generateCsvLines();

}
