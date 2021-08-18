import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Class file for a table model that contains a data series
 */
public class DataSeriesTableModel extends AbstractTableModel {
    private ArrayList<DataSeries> dataSeriesList;
    private ArrayList<DataPoint> displayedData_1;
    private ArrayList<DataPoint> displayedData_2;
    String[] columnNames;
    private final int ZERO = 0;
    private final int ONE = 1;
    private boolean moreThanOne;

    /**
     * Argument constructor
     * @param dataSeriesList list of data series to display to table
     * @param transformationType type of data transformation
     */
    public DataSeriesTableModel(ArrayList<DataSeries> dataSeriesList , TransformationType transformationType) {
        this.dataSeriesList = new ArrayList<>();
        this.dataSeriesList.addAll(dataSeriesList);
        this.moreThanOne = dataSeriesList.size() > 1;
        String nameQualifier = "";
        switch (transformationType) {
            case Level:
                this.displayedData_1 = dataSeriesList.get(ZERO).getRawData();
                if(this.moreThanOne)
                    this.displayedData_2 = dataSeriesList.get(ONE).getRawData();
                break;
            case MonthlyDelta:
                this.displayedData_1 = dataSeriesList.get(ZERO).getMonthlyChangeData();
                if(this.moreThanOne)
                    this.displayedData_2 = dataSeriesList.get(ONE).getMonthlyChangeData();
                nameQualifier = "Monthly Change";
                break;
            case YearlyDelta:
                this.displayedData_1 = dataSeriesList.get(ZERO).getYearlyChangeData();
                if(this.moreThanOne)
                    this.displayedData_2 = dataSeriesList.get(ONE).getYearlyChangeData();
                nameQualifier = "Yearly Change";
        }
        if(this.moreThanOne) this.columnNames = new String[4];
        else this.columnNames = new String[2]; 

        columnNames[0] = "Date";
        columnNames[1] = dataSeriesList.get(ZERO).getSeriesName() + " " + nameQualifier;
        if(this.moreThanOne) {
            columnNames[2] = "Date";
            columnNames[3] = dataSeriesList.get(ONE).getSeriesName() + " " + nameQualifier;
        }
    }

    /**
     * Get the number of rows
     * @return Number of rows to be displayed
     */
    @Override
    public int getRowCount() {
        return dataSeriesList.stream()
                .max(Comparator.comparing(s -> s.getRawData().size()))
                .get()
                .getRawData()
                .size();
    }

    /**
     * Get the number of columns
     * @return integer representation of the number of columns
     */
    @Override
    public int getColumnCount() {
        return dataSeriesList.size() * 2;
    }

    /**
     * Method to get the value at a particular "cell" of the data table
     * @param rowIndex integer representation of the of row
     * @param columnIndex integer representation of the column
     * @return Object to be displayed at the row/column location
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         switch(columnIndex) {
             case 0:
                 if(rowIndex > displayedData_1.size() - 1) return new Date(0);
                 return displayedData_1.get(rowIndex).getDate();
             case 1:
                 if(rowIndex > displayedData_1.size() - 1) return Double.NaN;
                 return displayedData_1.get(rowIndex).getData();
             case 2:
                 if(rowIndex >  displayedData_2.size() - 1) return new Date(0);
                 return displayedData_2.get(rowIndex).getDate();
             case 3:
                 if(rowIndex > displayedData_2.size() - 1) return new Date(0);
                 return displayedData_2.get(rowIndex).getData();
             default:
                return "-";
        }
    }

    /**
     * Get the name of a certain column
     * @param col integer value of the column to find the name of
     * @return String representation of the column's name
     */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Get the class of a column
     * @param col integer representation of the column
     * @return the Class type of the indicated column
     */
    public Class getColumnClass(int col) {
        if(col == 0 || col == 2) return Date.class;
        if(col == 1 || col == 3) return double.class;
        else return String.class;
    }
}
