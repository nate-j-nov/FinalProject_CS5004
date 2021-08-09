import jdk.jshell.spi.ExecutionControl;

import javax.xml.crypto.Data;
import java.sql.Array;
import java.util.ArrayList;

/**
 * Controller class for the application
 */
public class Controller {

    private ArrayList<DataSeries> availableDataSeries;
    private DataSeries selection;

    public Controller() {
        GdpSeriesGenerator gdpSeriesGenerator = new GdpSeriesGenerator();
        LaborMarketSeriesGenerator laborMarketSeriesGenerator = new LaborMarketSeriesGenerator();
        availableDataSeries = new ArrayList<DataSeries>();
        availableDataSeries.addAll(gdpSeriesGenerator.generateListOfDataSeries());
        availableDataSeries.addAll(laborMarketSeriesGenerator.generateListOfDataSeries());
    }

    /**
     * TODO: Add javadoc
     * @return
     */
    public ArrayList<DataSeries> getAvailableDataSeries() {
        return availableDataSeries;
    }

    public ArrayList<DataSeries> getAvailableDataSeriesAfterFilter(String textToFilter) {
        throw new UnsupportedOperationException();
    }
}
