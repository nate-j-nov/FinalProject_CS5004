import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Class file that represents a DataSeries, or a collection of data points along with other metadata.
 */
public class DataSeries {
    private String seriesName;
    private final String seriesCode;
    private Date startDate;
    private Date endDate;
    private String datasetName;
    private String provider;
    private ArrayList<DataPoint> rawData;
    private ArrayList<DataPoint> monthlyChangeData;
    private ArrayList<DataPoint> yearlyChangeData;
    private PeriodType periodType;
    private String datasetCode;
    private final DbnomicsApiClient dbnomicsApiClient;

    /**
     * Argument constructor for a DataSeries object
     * @param seriesCode String representation of the data series code used to query the data from the API
     * @param seriesName String representation of the name
     */
    public DataSeries(String seriesCode, String seriesName) {
        this.seriesName = seriesName;
        this.seriesCode = seriesCode;
        this.dbnomicsApiClient = new DbnomicsApiClient();
        monthlyChangeData = new ArrayList<>();
        yearlyChangeData = new ArrayList<>();
    }

    /**
     * toString override
     * @return string representation of the data series
     */
    public String toString() { return seriesName; }

    /**
     * Get the series code
     * @return String representation of the data series' code
     */
    public String getSeriesCode() {
        return seriesCode;
    }

    /**
     * Make a call to the API to retrieve the raw data.
     */
    public void retrieveRawData() {
        try {
            rawData = dbnomicsApiClient.getSeriesData(provider, datasetCode, seriesCode);
            startDate = rawData.get(0).getDate();
            endDate = rawData.get(rawData.size() - 1).getDate();
        } catch(ParseException pe) {
            System.out.println(pe.getMessage());
        }
    }

    /**
     * Calculates the monthly (or quarterly in the case of GDP)
     * For the data series
     */
    private void calculateMonthlyChange() {
        for (int i = 0; i < rawData.size(); i++) {
            Date date = rawData.get(i).getDate();
            double value;
            if(i < 1)
                continue;
            else {
                value = calculateDelta(rawData.get(i).getData(), rawData.get(i - 1).getData());
                BigDecimal bigDecimal = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
                value = bigDecimal.doubleValue();
            }
            monthlyChangeData.add(new DataPoint(date, value));
        }
    }

    /**
     * Calculates the annual change for teh data series
     */
    private void calculateYearlyChange() {
        for (int i = 0; i < rawData.size(); i++) {
            Date date = rawData.get(i).getDate();
            double value;
            if(periodType == PeriodType.Quarterly) {
                if(i < 4)
                    continue;
                else {
                    value = calculateDelta(rawData.get(i).getData(), rawData.get(i - 4).getData());
                    BigDecimal bigDecimal = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
                    value = bigDecimal.doubleValue();
                }
            } else {
                if(i < 12)
                    continue;
                else {
                    value = calculateDelta(rawData.get(i).getData(), rawData.get(i - 12).getData());
                    BigDecimal bigDecimal = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
                    value = bigDecimal.doubleValue();
                }
            }
            yearlyChangeData.add(new DataPoint(date, value));
        }
    }

    /**
     * Get the monthly change data
     * @return return ArrayList of data points representing the monthly change data
     */
    public ArrayList<DataPoint> getMonthlyChangeData() {
        if(monthlyChangeData.isEmpty()) calculateMonthlyChange();
        return monthlyChangeData;
    }

    /**
     * Get the yearly change data
     * @return return ArrayList of data points representing the yearly change data
     */
    public ArrayList<DataPoint> getYearlyChangeData() {
        if(yearlyChangeData.isEmpty()) calculateYearlyChange();
        return yearlyChangeData;
    }

    /**
     * Retrieve the name of the data provider
     * @return String representation of the provider name
     */
    public String getProviderName() {
        return provider;
    }

    /**
     * Get the name of the series
     * @return String representation of the name of the data series
     */
    public String getSeriesName() {
        return seriesName;
    }

    /**
     * Get the max of the dataSeries
     * @param transformationType indicates the transformation to calculate the max of
     * @return double representation of the max value
     */
    public double getMax(TransformationType transformationType) {
        switch (transformationType) {
            case Level:
                if(rawData.isEmpty()) retrieveRawData();
                return calculateMax(rawData);
            case MonthlyDelta:
                if(monthlyChangeData.isEmpty()) calculateMonthlyChange();
                return calculateMax(monthlyChangeData);
            case YearlyDelta:
                if(yearlyChangeData.isEmpty()) calculateYearlyChange();
                return calculateMax(yearlyChangeData);
            default:
                return 0;
        }
    }

    /**
     * Get the min of the dataSeries
     * @param transformationType indicates the transformation to calculate the min of
     * @return double representation of the min value
     */
    public double getMin(TransformationType transformationType) {
         switch (transformationType) {
            case Level:
                if(rawData.isEmpty()) retrieveRawData();
                return calculateMin(rawData);
            case MonthlyDelta:
                if(monthlyChangeData.isEmpty()) calculateMonthlyChange();
                return calculateMin(monthlyChangeData);
            case YearlyDelta:
                if(yearlyChangeData.isEmpty()) calculateYearlyChange();
                return calculateMin(yearlyChangeData);
            default:
                return 0;
        }
    }

    /**
     * Sets the period type
     * @param periodType new period type
     */
    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    /**
     * Sets the provider
     * @param provider String representation of the provider
     */
    public void setProvider(String provider) {
        this.provider = provider;
    }

    /**
     * Set the data set name
     * @param datasetName String of dataset name
     */
    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    /**
     * Set the dataset code
     * @param datasetCode new dataset code
     */
    public void setDataSetCode(String datasetCode) {
        this.datasetCode = datasetCode;
    }

    /**
     * Get the dataset code
     * @return String representation of the dataset code
     */
    public String getDatasetCode() {
        return datasetCode;
    }

    /**
     * Method to calculate the difference between two values
     * @param newVal double representation of the "new" value
     * @param oldVal double representation of the "old" value
     * @return double representation of the change between the values
     */
    private double calculateDelta(double newVal, double oldVal) {
        return (newVal / oldVal) - 1;
    }

    /**
     * Method to calculate the maximum of a set of data values
     * @param dataSeries ArrayList of data points of which this method finds the maximum
     * @return double representation of the max
     */
    private double calculateMax(ArrayList<DataPoint> dataSeries) {
        // This took so much playing around with
        return dataSeries.stream().max(Comparator.comparing(p -> p.getData())).get().getData();
    }

    /**
     * Method to calculate the min of a set of data values
     * @param dataSeries ArrayList of data points of which this method finds the min
     * @return double representation of the min
     */
    private double calculateMin(ArrayList<DataPoint> dataSeries) {
        return dataSeries.stream().min(Comparator.comparing(p -> p.getData())).get().getData();
    }

    /**
     * Sets the name of the series
     * @param seriesName String representation of the series name
     */
    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    /**
     * Get the level data of the data series (this is essentially what the API returns)
     * @return ArrayList of DataPoints of the raw data
     */
    public ArrayList<DataPoint> getRawData() {
        if(rawData.isEmpty()) retrieveRawData();
        return rawData;
    }
}
