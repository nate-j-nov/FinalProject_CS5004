import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

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

    public void retrieveRawData() {
        try {
            rawData = dbnomicsApiClient.getSeriesData(provider, datasetCode, seriesCode);
            startDate = rawData.get(0).getDate();
            endDate = rawData.get(rawData.size() - 1).getDate();
        } catch(ParseException pe) {
            System.out.println(pe.getMessage());
        }
    }

    private void calculateMonthlyChange() {
        for (int i = 0; i < rawData.size(); i++) {
            Date date = rawData.get(i).getDate();
            double value;
            if(i < 1)
                value = Double.NaN;
            else
                value = calculateDelta(rawData.get(i).getData(), rawData.get(i - 1).getData());

            monthlyChangeData.add(new DataPoint(date, value));
        }
    }

    private void calculateYearlyChange() {
        for (int i = 0; i < rawData.size(); i++) {
            Date date = rawData.get(i).getDate();
            double value;
            if(i < 12)
                value = Double.NaN;
            else
                value = calculateDelta(rawData.get(i).getData(), rawData.get(i - 12).getData());

            monthlyChangeData.add(new DataPoint(date, value));
        }
    }

    public ArrayList<DataPoint> getMonthlyChangeData() {
        if(monthlyChangeData == null) calculateMonthlyChange();
        return monthlyChangeData;
    }

    public ArrayList<DataPoint> getYearlyChangeData() {
        if(yearlyChangeData == null) calculateYearlyChange();
        return yearlyChangeData;
    }

    public String getProviderName() {
        return provider;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public double getMax(TransformationType transformationType) {
        switch(transformationType) {
            case Level:
                return calculateMax(rawData);

            case MonthlyDelta:
                return calculateMax(monthlyChangeData);

            case YearlyDelta:
                return calculateMax(yearlyChangeData);

            default:
                return 0;
        }
    }

    public double getMin(TransformationType transformationType) {
        switch(transformationType){
            case Level:
                return calculateMin(rawData);

            case MonthlyDelta:
                return calculateMin(monthlyChangeData);

            case YearlyDelta:
                return calculateMin(yearlyChangeData);

            default:
                return 0;
        }
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public void setDataSetCode(String datasetCode) {
        this.datasetCode = datasetCode;
    }

    public String getDatasetCode() {
        return datasetCode;
    }

    private double calculateDelta(double newVal, double oldVal) {
        return (newVal / oldVal) - 1;
    }

    private double calculateMax(ArrayList<DataPoint> dataSeries) {
        // This took so much playing around with
        return dataSeries.stream().max(Comparator.comparing(p -> p.getData())).get().getData();
    }

    private double calculateMin(ArrayList<DataPoint> dataSeries) {
        return dataSeries.stream().min(Comparator.comparing(p -> p.getData())).get().getData();
    }

    public String dataToString() {
        if(rawData == null) {
            retrieveRawData();
        }
        StringBuilder stringBuilder = new StringBuilder();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        stringBuilder.append(seriesName).append("\n");
        for (DataPoint dp : rawData) {
            stringBuilder.append(dateFormat.format(dp.getDate()) + " " + dp.getData() + "\n");
        }
        return stringBuilder.toString();
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }

    public ArrayList<DataPoint> getRawData() {
        return rawData;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
