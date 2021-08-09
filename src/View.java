import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/*
 * Created by JFormDesigner on Tue Aug 03 16:20:51 EDT 2021
 */

/*
dataSeriesDropdown.setModel(new DefaultComboBoxModel(displayedDataSeries.toArray()));
dataSeriesDropdown.setSelectedIndex(0);
*/


/**
 * @author Nate Novak
 */
public class View extends JFrame {
    private Controller controller;
    private ArrayList<DataSeries> displayedDataSeries;
    private DataSeries selectedDataSeries;
    private final int ZERO = 0;

    public View(Controller controller)
    {
        this.controller = controller;
        this.displayedDataSeries = controller.getAvailableDataSeries();
        this.selectedDataSeries = displayedDataSeries.get(ZERO);
        selectedDataSeries.retrieveRawData();
        initComponents();
        setComponentDetails();
        initEvents();
        initChart();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        dataSeriesDropdown = new JComboBox();
        SearchBar = new JTextField();
        searchButton = new JButton();
        outputMessageScrollPane = new JScrollPane();
        outputMessageTextPane = new JTextArea();
        getDataButton = new JButton();
        dataTableScrollPanel = new JScrollPane();
        dataTable = new JTable();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.add(dataSeriesDropdown);
        dataSeriesDropdown.setBounds(10, 145, 440, 30);

        //---- SearchBar ----
        SearchBar.setText("Search");
        contentPane.add(SearchBar);
        SearchBar.setBounds(590, 145, 500, 30);

        //---- searchButton ----
        searchButton.setText("Search");
        contentPane.add(searchButton);
        searchButton.setBounds(1100, 145, searchButton.getPreferredSize().width, 30);

        //======== outputMessageScrollPane ========
        {
            outputMessageScrollPane.setViewportView(outputMessageTextPane);
        }
        contentPane.add(outputMessageScrollPane);
        outputMessageScrollPane.setBounds(10, 700, 465, 175);

        //---- getDataButton ----
        getDataButton.setText("Get Data");
        contentPane.add(getDataButton);
        getDataButton.setBounds(465, 145, 110, 30);

        //======== dataTableScrollPanel ========
        {
            dataTableScrollPanel.setViewportView(dataTable);
        }
        contentPane.add(dataTableScrollPanel);
        dataTableScrollPanel.setBounds(610, 700, 835, 175);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void setComponentDetails() {
        // Set dropdown details
        dataSeriesDropdown.setModel(new DefaultComboBoxModel(displayedDataSeries.toArray()));
        dataSeriesDropdown.setSelectedIndex(ZERO);

        // Set dataTable details
        DataSeriesTableModel tableModel = new DataSeriesTableModel(selectedDataSeries, TransformationType.Level);
        dataTable.setModel(tableModel);

        // Set output text pane details
        outputMessageTextPane.setText("Data initialized.");
    }

    private void initEvents() {
        getDataButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(selectedDataSeries == dataSeriesDropdown.getSelectedItem()) {
                    outputMessageTextPane.append("\nData already displayed.");
                    return;
                }
                selectedDataSeries = (DataSeries) dataSeriesDropdown.getSelectedItem();
                outputMessageTextPane.append("\nRetrieving " + selectedDataSeries.getSeriesName() + " data...");
                selectedDataSeries.retrieveRawData();
                dataTable.setModel(new DataSeriesTableModel(selectedDataSeries, TransformationType.Level));
                // Update chart
                getContentPane().remove(chartPanel);
                initChart();
                chartPanel.revalidate();
                chartPanel.repaint();
                getContentPane().revalidate();
                outputMessageTextPane.append("\nRetrieval Successful.");
            }
        });
    }

    /**
     * I know this isn't great, but I needed to separate it from the initComponents because the autocode generation deletes
     * everything.
     */
    private void initChart() {
        DataSeriesChartWrapper chartWrapper = new DataSeriesChartWrapper(selectedDataSeries);
        chartPanel = chartWrapper.generateChart(TransformationType.Level);
        chartPanel.setPreferredSize(new Dimension(785, 440));
        chartPanel.setBounds(new Rectangle(10, 200, 1200, 450));
        getContentPane().add(chartPanel);
    }


    private ChartPanel dataSeriesChartPanel;
     // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
     // Generated using JFormDesigner Evaluation license - unknown
     private JComboBox dataSeriesDropdown;
     private JTextField SearchBar;
     private JButton searchButton;
     private JScrollPane outputMessageScrollPane;
     private JTextArea outputMessageTextPane;
     private JButton getDataButton;
     private JScrollPane dataTableScrollPanel;
     private JTable dataTable;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private ChartPanel chartPanel;
}
