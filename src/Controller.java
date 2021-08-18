import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Nate NOvak
 * CS5004 Summer 2021
 * Class file for a controller. Mostly just controls the data that's available for the
 * User to interact with
 */
public class Controller {
    private ArrayList<DataSeries> availableDataSeries;

    /**
     * No argument constructor that generates the list of data series available
     */
    public Controller() {
        GdpSeriesGenerator gdpSeriesGenerator = new GdpSeriesGenerator();
        LaborMarketSeriesGenerator laborMarketSeriesGenerator = new LaborMarketSeriesGenerator();
        availableDataSeries = new ArrayList<DataSeries>();
        availableDataSeries.addAll(gdpSeriesGenerator.generateListOfDataSeries());
        availableDataSeries.addAll(laborMarketSeriesGenerator.generateListOfDataSeries());
    }

    /**
     * Get the available data series generated in the constructor
     * @return ArrayList of available DataSeries
     */
    public ArrayList<DataSeries> getAvailableDataSeries() {
        return availableDataSeries;
    }

    /**
     * Generates a list of data series available based on the filter from the search bar
     * @param textToFilter String of text used to filter the available data series
     * @return ArrayList of DataSeries objects based on the filter
     */
    public ArrayList<DataSeries> getAvailableDataSeriesAfterFilter(String textToFilter) {
        ArrayList<DataSeries> returnSeries = (ArrayList<DataSeries>) availableDataSeries.stream().filter(d -> d.getSeriesName()
                .toLowerCase().contains(textToFilter)).
                collect(Collectors.toList());
        if(returnSeries.isEmpty()) {
            return availableDataSeries;
        }
        return returnSeries;
    }
}
