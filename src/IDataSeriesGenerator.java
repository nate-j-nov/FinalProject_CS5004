import java.util.ArrayList;

/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Interface for a DataSeries generator
 */
public interface IDataSeriesGenerator {
    /**
     * Generate a list of data series from from the GDP datasets
     * @return ArrayList of dataSeries generated from the dataset
     */
    ArrayList<DataSeries> generateListOfDataSeries();
}
