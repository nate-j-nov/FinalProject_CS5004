import org.jfree.chart.ChartPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private ArrayList<DataSeries> selectedDataSeries;
    private final int ZERO = 0;
    private TransformationType selectedTransformation;

    public View(Controller controller)
    {
        this.controller = controller;
        this.displayedDataSeries = controller.getAvailableDataSeries();
        this.selectedDataSeries = new ArrayList<>();
        this.selectedDataSeries.add(displayedDataSeries.get(ZERO));
        this.selectedTransformation = TransformationType.Level;
        this.selectedDataSeries.get(ZERO).retrieveRawData();
        this.transformationTypes = new TransformationType[] {
                TransformationType.Level,
                TransformationType.MonthlyDelta,
                TransformationType.YearlyDelta
        };
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
        transformationComboBox = new JComboBox();
        transformButton = new JButton();
        logoPanel = new JLabel();
        clearSearchButton = new JButton();
        downloadButton = new JButton();
        label1 = new JLabel();
        addDataButton = new JButton();

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
        searchButton.setBounds(1100, 145, 95, 30);

        //======== outputMessageScrollPane ========
        {
            outputMessageScrollPane.setViewportView(outputMessageTextPane);
        }
        contentPane.add(outputMessageScrollPane);
        outputMessageScrollPane.setBounds(10, 725, 465, 175);

        //---- getDataButton ----
        getDataButton.setText("Get Data");
        contentPane.add(getDataButton);
        getDataButton.setBounds(465, 145, 110, 30);

        //======== dataTableScrollPanel ========
        {
            dataTableScrollPanel.setViewportView(dataTable);
        }
        contentPane.add(dataTableScrollPanel);
        dataTableScrollPanel.setBounds(610, 725, 835, 175);
        contentPane.add(transformationComboBox);
        transformationComboBox.setBounds(10, 185, 165, 30);

        //---- transformButton ----
        transformButton.setText("Transform Data");
        contentPane.add(transformButton);
        transformButton.setBounds(215, 185, 165, 30);

        //---- logoPanel ----
        logoPanel.setIcon(null);
        contentPane.add(logoPanel);
        logoPanel.setBounds(10, 5, 235, 140);

        //---- clearSearchButton ----
        clearSearchButton.setText("Clear Search");
        contentPane.add(clearSearchButton);
        clearSearchButton.setBounds(1235, 145, 140, 30);

        //---- downloadButton ----
        downloadButton.setText("Download Data");
        contentPane.add(downloadButton);
        downloadButton.setBounds(440, 185, 165, 30);

        //---- label1 ----
        label1.setText("To zoom, scroll the mousewheel where you want to zoom in or out");
        contentPane.add(label1);
        label1.setBounds(10, 695, 420, label1.getPreferredSize().height);

        //---- addDataButton ----
        addDataButton.setText("Add Data");
        contentPane.add(addDataButton);
        addDataButton.setBounds(635, 185, 135, 30);

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

        // Set transformation type details
        transformationComboBox.setModel(new DefaultComboBoxModel(transformationTypes));
        transformationComboBox.setSelectedIndex(ZERO);

        // Set logo details
        // Followed this SO post: https://stackoverflow.com/a/16345968/15471892
        BufferedImage image;
        try {
            image = ImageIO.read(new File("src/logo.png"));
            Image logo = image.getScaledInstance(logoPanel.getWidth(), logoPanel.getHeight(), Image.SCALE_SMOOTH);
            logoPanel.setIcon(new ImageIcon(logo));
        } catch (IOException e) {
            outputMessageTextPane.append("\nUnable to load load the logo");
            e.printStackTrace();
        }

        // Set clear search button details
        clearSearchButton.setVisible(false);
    }

    private void initEvents() {
        getDataButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(selectedDataSeries == dataSeriesDropdown.getSelectedItem()) {
                    outputMessageTextPane.append("\nData already displayed.");
                    return;
                }
                // Update the data table.
                selectedDataSeries.clear();
                selectedDataSeries.add((DataSeries) dataSeriesDropdown.getSelectedItem());
                selectedTransformation = TransformationType.Level;
                outputMessageTextPane.append("\nRetrieving " + selectedDataSeries.get(ZERO).getSeriesName() + " data...");
                selectedDataSeries.get(ZERO).retrieveRawData();
                dataTable.setModel(new DataSeriesTableModel(selectedDataSeries, selectedTransformation));
                // Update chart
                updateChart();
                transformationComboBox.setSelectedIndex(ZERO);
                outputMessageTextPane.append("\nRetrieval Successful.");
            }
        });

        transformButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(selectedTransformation == transformationComboBox.getSelectedItem()) {
                    outputMessageTextPane.append("\nAlready displaying this transformation for " + selectedDataSeries.get(ZERO).getSeriesName() + ".");
                    return;
                }
                selectedTransformation = (TransformationType) transformationComboBox.getSelectedItem();
                outputMessageTextPane.append("\nRetrieving " + selectedTransformation + " data...");
                // Update data table.
                dataTable.setModel(new DataSeriesTableModel(selectedDataSeries, selectedTransformation));
                // Update the chart
                updateChart();
                outputMessageTextPane.append("\n" + selectedTransformation + " data retrieved...");
            }
        });

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String searchText = SearchBar.getText();
                if(searchText.equals("Search") || searchText.isBlank() || searchText.isEmpty()) {
                    outputMessageTextPane.append("\nInvalid search...");
                    SearchBar.setText("Search");
                    return;
                }
                clearSearchButton.setVisible(true);
                displayedDataSeries = controller.getAvailableDataSeriesAfterFilter(searchText.toLowerCase());
                dataSeriesDropdown.setModel(new DefaultComboBoxModel(displayedDataSeries.toArray()));
                dataSeriesDropdown.setSelectedIndex(0);
                outputMessageTextPane.append("\nSearch complete for \"" + searchText + "\"...");
            }
        });

        clearSearchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SearchBar.setText("Search");
                displayedDataSeries = controller.getAvailableDataSeries();
                dataSeriesDropdown.setModel(new DefaultComboBoxModel(displayedDataSeries.toArray()));
                dataSeriesDropdown.setSelectedIndex(0);
                clearSearchButton.setVisible(false);
                outputMessageTextPane.append("\nSearch cleared...");
            }
        });

        downloadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                outputMessageTextPane.append("\nDownloading CSV for " + selectedDataSeries.get(ZERO).getSeriesName());
                DataSeriesCsvGenerator csvGenerator = new DataSeriesCsvGenerator(selectedDataSeries);
                outputMessageTextPane.append("\n" + csvGenerator.generateCsvFile());
            }
        });

        addDataButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                DataSeries dataToBeAdded = (DataSeries) dataSeriesDropdown.getSelectedItem();
                if(selectedDataSeries.size() == 2) {
                    selectedDataSeries.remove(1);
                }
                if(dataToBeAdded == selectedDataSeries.get(ZERO)) {
                    outputMessageTextPane.append("\nData is already displayed...");
                    return;
                }
                outputMessageTextPane.append("\nAdding data for " + dataToBeAdded.getSeriesName() + "...");
                dataToBeAdded.retrieveRawData();
                if(!dataToBeAdded.getRawData().isEmpty()) {
                    selectedDataSeries.add(dataToBeAdded);
                    dataTable.setModel(new DataSeriesTableModel(selectedDataSeries, selectedTransformation));
                    updateChart();
                    outputMessageTextPane.append("\nSuccessfully added");
                } else {
                    outputMessageTextPane.append("\nUnable to add " + dataToBeAdded.getSeriesName());
                }
            }
        });
    }

    /**
     * I know this isn't great, but I needed to separate it from the initComponents because the autocode generation deletes
     * everything.
     */
    private void initChart() {
        DataSeriesChartWrapper chartWrapper = new DataSeriesChartWrapper(selectedDataSeries);
        chartPanel = chartWrapper.generateChart(selectedTransformation);
        chartPanel.setPreferredSize(new Dimension(785, 440));
        chartPanel.setBounds(new Rectangle(10, 250, 1200, 425));
        getContentPane().add(chartPanel);
    }

    private void updateChart() {
        getContentPane().remove(chartPanel);
        initChart();
        chartPanel.revalidate();
        chartPanel.repaint();
        getContentPane().revalidate();
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
     private JComboBox transformationComboBox;
     private JButton transformButton;
     private JLabel logoPanel;
     private JButton clearSearchButton;
     private JButton downloadButton;
     private JLabel label1;
     private JButton addDataButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    private ChartPanel chartPanel;
    private TransformationType[] transformationTypes;
}
