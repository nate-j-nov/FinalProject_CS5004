import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class JFreeChartTest extends JFrame {
    private DataSeries dataSeries;
    public JFreeChartTest(DataSeries dataSeries) {
        this.dataSeries = dataSeries;
        dataSeries.retrieveRawData();
        initUI();
    }

    private void initUI() {

        XYDataset dataset = createDataset();
        JFreeChart chart = createChart(dataset);
        XYPlot plot = chart.getXYPlot();

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        ValueAxis valueAxis = plot.getRangeAxis();
        valueAxis.setRange(dataSeries.getMin(TransformationType.Level), dataSeries.getMax(TransformationType.Level));
        plot.setRangeAxis(valueAxis);

        ChartPanel chartPanel = new ChartPanel(chart);
        //chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        add(chartPanel);
        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private TimeSeriesCollection createDataset() {
        TimeSeries timeSeries = new TimeSeries("Name");
        ArrayList<DataPoint> data;

        data = dataSeries.getRawData();

        System.out.println(dataSeries.dataToString());

        for(DataPoint d : data) {
            timeSeries.add(new Day(d.getDate()), d.getData());
        }

        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        timeSeriesCollection.addSeries(timeSeries);
        return timeSeriesCollection;

    }

    private JFreeChart createChart(XYDataset dataset) {

        JFreeChart chart = ChartFactory.createTimeSeriesChart("Hello", "HI", "HI", dataset);

        /*XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle("Average Salary per Age",
                        new Font("Serif", java.awt.Font.BOLD, 18)
                )
        );*/

        return chart;
    }

    public static void main(String[] args) {
        GdpSeriesGenerator gen = new GdpSeriesGenerator();
        ArrayList<DataSeries> availableDataSeries = gen.generateListOfDataSeries();

        EventQueue.invokeLater(() -> {

            var ex = new JFreeChartTest(availableDataSeries.get(0));
            ex.setVisible(true);
        });
    }
}