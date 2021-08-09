
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GdpSeriesGenerator extends DataSeriesGenerator{
    public GdpSeriesGenerator() {
        super();
    }

    /**
     * Generate the series in the GDP dataset
     * @return ArrayList of data series contained in the GDP series.
     */
    @Override
    public ArrayList<DataSeries> generateListOfDataSeries() {
        String datasetCode = "NIPA-T10105";
        String provider = "BEA";
        ArrayList<DataSeries> allSeries = dbnomicsApiClient.getSeriesNamesFromDataSetMetaData(provider, datasetCode);

        Predicate<DataSeries> quarterlyOnly = series -> series.getSeriesCode().contains("-Q");

        // Filter out annual data series.
        ArrayList<DataSeries> selectedSeries = (ArrayList<DataSeries>) allSeries.stream().filter(quarterlyOnly).collect(Collectors.toList());

        // Clean up names of GDP data
        cleanDataSeriesNames(selectedSeries);

        // Sort the list of data series by line number
        Collections.sort(selectedSeries, (d1, d2) -> {
            return (int) (findLineNumber(d1) - findLineNumber(d2)); }
        );

        // Set further details.
        setProviderAndDatasetDetails(selectedSeries, provider, "National Income and Product Accounts", datasetCode, PeriodType.Quarterly);

        return selectedSeries;
    }

    /**
     * Clean up the GDP names so "- Quarterly" is removed.
     * @param gdpDataSeries ArrayList of GDP data
     */
    protected void cleanDataSeriesNames(ArrayList<DataSeries> dataSeries) {
        for (DataSeries ds : dataSeries) {
            String currentName = ds.getSeriesName();
            int indexOfDash = currentName.indexOf("-");
            String newName = "GDP: " + currentName.substring(0, indexOfDash - 1) + " [Millions]";
            ds.setSeriesName(newName);
        }
    }

    /**
     * Method to find the line number in the name and
     * @param gdpDataSeries Gdp data series
     * @return integer representation of the series' line number in the report.
     */
    private int findLineNumber(DataSeries gdpDataSeries) {
        String seriesName = gdpDataSeries.getSeriesName();
        String numberString = "";
        for (int i = 0; i < seriesName.length() - 1; i++) {
            if(Character.isDigit(seriesName.charAt(i))) {
                numberString += seriesName.charAt(i);
            }
        }
        return Integer.parseInt(numberString);
    }
}
