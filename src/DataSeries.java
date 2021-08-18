import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
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
                continue;
            else {
                value = calculateDelta(rawData.get(i).getData(), rawData.get(i - 1).getData());
                BigDecimal bigDecimal = new BigDecimal(value).setScale(4, RoundingMode.HALF_UP);
                value = bigDecimal.doubleValue();
            }
            monthlyChangeData.add(new DataPoint(date, value));
        }
    }

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

    public ArrayList<DataPoint> getMonthlyChangeData() {
        if(monthlyChangeData.isEmpty()) calculateMonthlyChange();
        return monthlyChangeData;
    }

    public ArrayList<DataPoint> getYearlyChangeData() {
        if(yearlyChangeData.isEmpty()) calculateYearlyChange();
        return yearlyChangeData;
    }

    public String getProviderName() {
        return provider;
    }

    public String getSeriesName() {
        return seriesName;
    }

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
        if(rawData.isEmpty()) {
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
        if(rawData.isEmpty()) retrieveRawData();
        return rawData;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PeriodType getPeriodType() {
        return periodType;
    }

}
