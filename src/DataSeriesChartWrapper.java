import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
This was used for help: https://zetcode.com/java/jfreechart/
 */

/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Class to handle charting the data in the view
 */
public class DataSeriesChartWrapper  {
    private final ArrayList<DataSeries> dataSeriesList;
    private JFreeChart chart;
    private boolean moreThanOne;
    private final int ZERO = 0;
    private final int ONE = 1;

    /**
     * Argument constructor for the DataSeriesChartWrapper
     * @param dataSeriesList ArrayList of DataSeries objects to be charted
     */
    public DataSeriesChartWrapper(ArrayList<DataSeries> dataSeriesList) {
        this.dataSeriesList = new ArrayList<>();
        this.dataSeriesList.addAll(dataSeriesList);
        this.moreThanOne = dataSeriesList.size() > 1;
    }

    /**
     * Generates a ChartPanel of the data
     * @param transformationType type of transformation
     * @return ChartPanel object of the data
     */
    public ChartPanel generateChart(TransformationType transformationType) {

        String primaryYAxisLabel = getYAxisLabel(dataSeriesList.get(ZERO), transformationType);

        // Generate the data
        XYDataset dataset = generateData(transformationType);

        String chartTitle = getChartTitle(dataSeriesList.get(ZERO), transformationType);

        if(moreThanOne) {
            chartTitle += " VS. " + getChartTitle(dataSeriesList.get(ONE), transformationType);
        }

        // Create the chart
        chart = ChartFactory.createTimeSeriesChart(chartTitle, "Date", primaryYAxisLabel, dataset, true, true, false);

        // Set settings for plot
        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        // Add tooltips
        renderer.setDefaultToolTipGenerator(new XYToolTipGenerator() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
            @Override
            public String generateToolTip(XYDataset xyDataset, int i, int i1) {
                return  dateFormat.format((xyDataset.getXValue(i, i1)))
                        + " | "
                        + xyDataset.getYValue(i, i1);
            }
        });

        // Set various formatting options
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        if(moreThanOne) {
            renderer.setSeriesPaint(1, Color.RED);
            renderer.setSeriesShapesVisible(1, false);
            renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        }

        plot.setRenderer(renderer);

        plot.setBackgroundPaint(Color.WHITE);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Set date axis options
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        // Set value axis options
        plot.setRangeAxis(0, new NumberAxis(primaryYAxisLabel));
        plot.mapDatasetToRangeAxis(ZERO, ZERO);
        if(moreThanOne) {
            String secondaryYAxisLabel = getYAxisLabel(dataSeriesList.get(ONE), transformationType);
            plot.setRangeAxis(1, new NumberAxis(secondaryYAxisLabel));
            ValueAxis secondAxis = plot.getRangeAxis(1);
            double min = dataSeriesList.get(ONE).getMin(transformationType);
            double max = dataSeriesList.get(ONE).getMax(transformationType);
            secondAxis.setRange(min, max);
            plot.mapDatasetToRangeAxis(ONE, ONE);
        }

        // Set the chart panel settings
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setMouseWheelEnabled(true);

        return chartPanel;
    }

    /**
     * Generates the data to be charted
     * @param transformationType type of transformation
     * @return TimeSeriesCollection of the data series to be charted.
     */
    private TimeSeriesCollection generateData(TransformationType transformationType) {

        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        timeSeriesCollection.addSeries(configureTimeSeries(dataSeriesList.get(ZERO), transformationType));

        if(moreThanOne)
            timeSeriesCollection.addSeries(configureTimeSeries(dataSeriesList.get(ONE), transformationType));

        return timeSeriesCollection;
    }

    /**
     * Method to set the time series with the data from the dataseries
     * @param dataSeries DataSeries to chart
     * @param transformationType type of transformation
     * @return TimeSeries object of the data series data
     */
    private TimeSeries configureTimeSeries(DataSeries dataSeries, TransformationType transformationType) {
        TimeSeries timeSeries = new TimeSeries(dataSeries.getSeriesName());

        // Wow just learned about switch expressions, very cool
        ArrayList<DataPoint> data_1 = switch (transformationType) {
            case Level -> dataSeries.getRawData();
            case MonthlyDelta -> dataSeries.getMonthlyChangeData();
            case YearlyDelta -> dataSeries.getYearlyChangeData();
        };

        for(DataPoint d : data_1) {
            timeSeries.add(new Day(d.getDate()), d.getData());
        }

        return timeSeries;
    }

    /**
     * Determine a Y Axis label
     * @param dataSeries DataSeries being mapped to the axis
     * @param transformationType type of data transformation
     * @return String of the label
     */
    private String getYAxisLabel(DataSeries dataSeries, TransformationType transformationType) {
        if(transformationType == TransformationType.Level && dataSeries.getProviderName().equals("BEA")) return "$ Millions";
        else if(transformationType == TransformationType.Level && dataSeries.getProviderName().equals("BLS")) return "Thousands";
        else return "Percent";
    }

    /**
     * Generates the chart title
     * @param dataSeries dataseries being charted
     * @param transformationType type of data transformation
     * @return string representation of a data series' title in the context of the chart
     */
    private String getChartTitle(DataSeries dataSeries, TransformationType transformationType) {
        return switch (transformationType) {
            case Level -> dataSeries.getSeriesName();
            case MonthlyDelta -> dataSeries.getSeriesName() + " Monthly Change";
            case YearlyDelta -> dataSeries.getSeriesName() + " Yearly Change";
        };
    }
}
