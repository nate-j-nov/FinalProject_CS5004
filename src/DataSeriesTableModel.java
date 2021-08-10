import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

public class DataSeriesTableModel extends AbstractTableModel {
    private DataSeries dataSeries;
    private ArrayList<DataPoint> displayedData;
    String[] columnNames = new String[2];


    public DataSeriesTableModel(DataSeries dataSeries, TransformationType transformationType) {
        this.dataSeries = dataSeries;
        String nameQualifier = "";
        switch (transformationType) {
            case Level:
                this.displayedData = dataSeries.getRawData();
                break;
            case MonthlyDelta:
                this.displayedData = dataSeries.getMonthlyChangeData();
                nameQualifier = "Monthly Change";
                break;
            case YearlyDelta:
                this.displayedData = dataSeries.getYearlyChangeData();
                nameQualifier = "Yearly Change";
        }
        columnNames[0] = "Date";
        columnNames[1] = dataSeries.getSeriesName() + " " + nameQualifier;
    }

    @Override
    public int getRowCount() {
        if(dataSeries == null) return 0;
        else return displayedData.size();
    }

    @Override
    public int getColumnCount() {
        return 2; // This will change
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0) {
            return displayedData.get(rowIndex).getDate();
        }

        return displayedData.get(rowIndex).getData();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnClass(int col) {
        if(col == 0) return Date.class;
        else return double.class;
    }
}
