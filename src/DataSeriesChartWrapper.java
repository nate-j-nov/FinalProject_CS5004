import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/*
This was used for help: https://zetcode.com/java/jfreechart/
 */
public class DataSeriesChartWrapper  {
    private final DataSeries dataSeries;
    private JFreeChart chart;

    public DataSeriesChartWrapper(DataSeries dataSeries) {
        this.dataSeries = dataSeries;
    }

    // When the transformation changes, just call generateChart again.
    // When new data is selected, create an entirely new chart.
    public ChartPanel generateChart(TransformationType transformationType) {

        String yAxisLabel;
        if(transformationType == TransformationType.Level && dataSeries.getProviderName().equals("BEA")) yAxisLabel = "Millions";
        else if(transformationType == TransformationType.Level && dataSeries.getProviderName().equals("BLS")) yAxisLabel = "Thousands";
        else yAxisLabel = "Percent";


        XYDataset dataset = generateData(transformationType);

        // Create the chart
        chart = ChartFactory.createTimeSeriesChart(dataSeries.getSeriesName(), "Date", yAxisLabel, dataset, false, true, false);

        String chartTitle = switch (transformationType) {
            case Level -> dataSeries.getSeriesName();
            case MonthlyDelta -> dataSeries.getSeriesName() + " Monthly Change";
            case YearlyDelta -> dataSeries.getSeriesName() + " Yearly Change";
        };
        chart.setTitle(new TextTitle(chartTitle));

        // Set settings for plot
        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, false);

        plot.setRenderer(renderer);

        plot.setBackgroundPaint(Color.WHITE);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Set date axis options
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        // Set value axis options
        ValueAxis valueAxis = plot.getRangeAxis();
        double min = dataSeries.getMin(transformationType) - 0.1;
        double max = dataSeries.getMax(transformationType);
        double average = (min + max) / 2;

        double axisMin = min - 0.1 * average;
        double axisMax = max + 0.1 * average;

        valueAxis.setRange(axisMin, axisMax);

        // Set the chart panel settings
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setMouseZoomable(false);
        return chartPanel;
    }

    private TimeSeriesCollection generateData(TransformationType transformationType) {
        TimeSeries timeSeries = new TimeSeries("Name");

        // Wow just learned about switch expressions, very cool
        ArrayList<DataPoint> data = switch (transformationType) {
            case Level -> dataSeries.getRawData();
            case MonthlyDelta -> dataSeries.getMonthlyChangeData();
            case YearlyDelta -> dataSeries.getYearlyChangeData();
        };

        for(DataPoint d : data) {
            timeSeries.add(new Day(d.getDate()), d.getData());
        }

        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        timeSeriesCollection.addSeries(timeSeries);
        return timeSeriesCollection;
    }

}
