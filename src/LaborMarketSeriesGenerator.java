

import java.util.ArrayList;

public class LaborMarketSeriesGenerator extends DataSeriesGenerator{

    public LaborMarketSeriesGenerator() {
        super();
    }
    /**
     * Generate the Labor market data series
     * @return ArrayList of data series from the BLS
     */
    @Override
    public ArrayList<DataSeries> generateListOfDataSeries() {
        // DBNomics uses the same naming scheme for their data series as the BLS.
        // So this is based on their mapping mechanism : https://www.bls.gov/help/hlpforma.htm
        String prefix = "CE";
        String seasonalAdjustmentCode = "S";
        String[] superSectorCodes  = {"00", "05", "06", "07", "08", "10",
                "20", "30", "31", "32", "40", "41", "42",
                "43", "44", "50", "55", "60", "65", "70",
                "80", "90"};
        String industryCode = "000000";
        String dataType = "01";

        // Generate the seriesID
        ArrayList<String> seriesIds = new ArrayList<>();
        for (String superSectorCode : superSectorCodes) {
            String seriesId = prefix + seasonalAdjustmentCode + superSectorCode + industryCode + dataType;
            seriesIds.add(seriesId);
        }

        String datasetCode = "ce";
        String provider = "BLS";
        ArrayList<DataSeries> selectedSeries = dbnomicsApiClient.getSeriesNamesFromSeriesCode(provider, datasetCode, seriesIds);

        // Clean the names of the data series.
        cleanDataSeriesNames(selectedSeries);

        setProviderAndDatasetDetails(selectedSeries, provider, "Current Employment Statistics", datasetCode, PeriodType.Monthly);

        return selectedSeries;
    }

    /**
     * Clean up the GDP names so extraneous parts are is removed. TODO: put this in abstract class.
     * @param dataSeries ArrayList of GDP data
     */
    protected void cleanDataSeriesNames(ArrayList<DataSeries> dataSeries) {
        for (DataSeries ds : dataSeries) {
            String currentName = ds.getSeriesName();
            // Find first dash in the name
            int indexOfFirstDash = currentName.indexOf("–");
            String trimmedName = currentName.substring(indexOfFirstDash + 2);

            // Find the second dash in the name
            int indexOfSecondDash = trimmedName.indexOf("–");
            String newName = "Employment: " + trimmedName.substring(0, indexOfSecondDash) + " [Thousands]";
            ds.setSeriesName(newName);
        }
    }
}
