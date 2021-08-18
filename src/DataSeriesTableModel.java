import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

public class DataSeriesTableModel extends AbstractTableModel {
    private ArrayList<DataSeries> dataSeriesList;
    private ArrayList<DataPoint> displayedData_1;
    private ArrayList<DataPoint> displayedData_2;
    String[] columnNames;
    private final int ZERO = 0;
    private final int ONE = 1;
    private boolean moreThanOne;

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

    @Override
    public int getRowCount() {
        return dataSeriesList.stream()
                .max(Comparator.comparing(s -> s.getRawData().size()))
                .get()
                .getRawData()
                .size();
    }

    @Override
    public int getColumnCount() {
        return dataSeriesList.size() * 2;
    }

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

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnClass(int col) {
        if(col == 0 || col == 2) return Date.class;
        if(col == 1 || col == 3) return double.class;
        else return String.class;
    }
}
