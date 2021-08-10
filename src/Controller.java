import jdk.jshell.spi.ExecutionControl;

import javax.xml.crypto.Data;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

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
        ArrayList<DataSeries> returnSeries = new ArrayList<>();
        returnSeries.addAll(availableDataSeries);
        return (ArrayList<DataSeries>) returnSeries.stream().filter(d -> d.getSeriesName()
                .toLowerCase().contains(textToFilter)).
                collect(Collectors.toList());
    }
}
