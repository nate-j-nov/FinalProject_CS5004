
/**
 * @author Nate Novak
 * 7/24/2021
 * Class file making calls to the DBNomics API
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DbnomicsApiClient {

    /**
     * String representation of the base URL
     */
    private final String baseUrl;

    /**
     * ApiHandler object
     */
    private final ApiHandler apiHandler;

    /**
     * No argument constructor
     */
    public DbnomicsApiClient() {
        this.baseUrl = "http://api.db.nomics.world/v22/";
        this.apiHandler = new ApiHandler(baseUrl);
    }

    // https://howtodoinjava.com/java/library/json-simple-read-write-json-examples/ was used for help on this

    /**
     * Retrieve a mapping of the data series available in a dataset given the provider and dataset
     * @param provider String representation of the provider. Used for making call to API
     * @param dataset String representation of the dataset code
     * @return
     */
    public ArrayList<DataSeries> getSeriesNamesFromDataSetMetaData(String provider, String dataset) {
        String seriesUrl = "series/" + provider + "/" + dataset;
        return processMetaData(seriesUrl);
    }

    /**
     * Retrieves the identifier (series_code) for that series
     * @param doc JSONObject of a specific series
     * @return KeyValuePair object with the series code and name
     */
    private KeyValuePair<String, String> retrieveSeriesCode(JSONObject doc) {
        String seriesCode = (String) doc.get("series_code");
        String seriesName = (String) doc.get("series_name");
        return new KeyValuePair<String, String>(seriesCode, seriesName);
    }

    /**
     * Retrieve the names and Ids of series given the code
     * @param provider String representation of the data provider
     * @param dataset String representation of the dataset
     * @param seriesIds List of series to get data from
     * @return ArrayList of DataSeries
     */
    public ArrayList<DataSeries> getSeriesNamesFromSeriesCode(String provider, String dataset, ArrayList<String> seriesIds) {
        ArrayList<DataSeries> dataSeriesList = new ArrayList<>();

        // Build the rest of the URL to get to the endpoint
        String seriesUrl = "series?series_ids=";
        for (int i = 0; i < seriesIds.size(); i++) {
            seriesUrl += provider + "%2F" + dataset +"%2F" + seriesIds.get(i);
            if(i != seriesIds.size() - 1) {
                seriesUrl+=",";
            }
        }
        seriesUrl += "&facets=true&observations=false&format=json&limit=1000&offset=0";
        return processMetaData(seriesUrl);
    }

    /**
     * Call DBNomics API and generate data series from the API call.
     * Extracts the name and code from the Json response.
     * @param seriesUrl URL to reach the API endpoint
     * @return ArrayList of DataSeries available
     */
    private ArrayList<DataSeries> processMetaData(String seriesUrl) {
        ArrayList<DataSeries> dataSeriesList = new ArrayList<>();
        JSONObject apiResponse = apiHandler.makeRequest(seriesUrl);

        JSONObject series = (JSONObject) apiResponse.get("series");

        JSONArray docs = (JSONArray) series.get("docs");

        for(Object obj : docs) {
            KeyValuePair<String, String> pair = retrieveSeriesCode((JSONObject) obj);
            dataSeriesList.add(new DataSeries(pair.getKey(), pair.getValue()));
        }

        return dataSeriesList;
    }

    /**
     * Retrieve data values from the DBNomics API
     * @param provider String representation of the provider
     * @param datasetCode String representation of the dataset code
     * @param seriesCode String representation of the series code
     * @return List of DataPoint objects from teh parsed data.
     * @throws ParseException
     */
    public ArrayList<DataPoint> getSeriesData(String provider, String datasetCode, String seriesCode) throws ParseException {
        ArrayList<DataPoint> dataPoints = new ArrayList<>();

        String seriesUrl = "series?series_ids=" + provider + "%2F" + datasetCode + "%2F" +
                seriesCode + "&observations=true&metadata=false&format=json&limit=1000&offset=0";

        JSONObject apiResponse = (JSONObject) apiHandler.makeRequest(seriesUrl);
        JSONObject series = (JSONObject) apiResponse.get("series");
        JSONArray docs = (JSONArray) series.get("docs");

        JSONObject wrapperLayer = (JSONObject) docs.get(0);
        JSONArray dates = (JSONArray) wrapperLayer.get("period_start_day");
        JSONArray values = (JSONArray) wrapperLayer.get("value");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < dates.size(); i++) {
            String dateString = (String) dates.get(i);
            double value = (double) values.get(i);

            Date date = dateFormat.parse(dateString);

            dataPoints.add(new DataPoint(date, value));
        }
        return dataPoints;
    }
}
