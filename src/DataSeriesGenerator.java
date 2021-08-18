import java.util.ArrayList;
/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Class to generate the data series that will be available in the application
 */
public abstract class DataSeriesGenerator implements IDataSeriesGenerator {

    protected final DbnomicsApiClient dbnomicsApiClient;
    public DataSeriesGenerator() {
        dbnomicsApiClient = new DbnomicsApiClient();
    }

    /**
     * Set further details of the data series
     * @param dataSeriesList List of data series to set these details
     * @param provider String representation of the provider
     * @param datasetName String representation of the dataset
     * @param periodType Indicates the timeliness of data releases, ie quarterly or monthly
     */
    protected void setProviderAndDatasetDetails(ArrayList<DataSeries> dataSeriesList, String provider, String datasetName, String datasetCode, PeriodType periodType) {
        for(DataSeries ds : dataSeriesList) {
            ds.setProvider(provider);
            ds.setDatasetName(datasetName);
            ds.setPeriodType(periodType);
            ds.setDataSetCode(datasetCode);
        }
    }

    /**
     * Method to clean and set the names of each data series from the API call
     * @param dataSeries DataSeries object
     */
    protected abstract void cleanDataSeriesNames(ArrayList<DataSeries> dataSeries);
}
