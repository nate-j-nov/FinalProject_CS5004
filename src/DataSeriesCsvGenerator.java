import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class DataSeriesCsvGenerator extends CsvGeneratorService {
    private DataSeries dataSeries;
    private SimpleDateFormat dateFormat;

    public DataSeriesCsvGenerator(DataSeries dataSeries) {
        super();
        this.dataSeries = dataSeries;
        this.dateFormat = new SimpleDateFormat("MM-yyyy");
    }

    @Override
    public String generateCsvFile() {
        Date now = new Date();

        String fileName = dataSeries.getSeriesCode() + "_"  + dateFormat.format(now) + ".csv";
        generateCsvLines();

        try {
            FileWriter writer = new FileWriter(fileName);

            // Set the header information for the file
            writer.append("SeriesName: ")
                    .append(dataSeries.getSeriesCode())
                    .append(",Series Code: ")
                    .append(dataSeries.getSeriesCode())
                    .append("\n\n");

            // Set the column headings
            writer.append("Date,Value\n");
            for(String[] line : lines) {
                writer.append(convertToCsvLine(line)).append("\n");
            }
            writer.flush();
            writer.close();
            return "CSV file successfully generated.";
        } catch(IOException ioe) {
            return "Unable to generate CSV file.";
        }
    }

    @Override
    protected void generateCsvLines() {
        for(DataPoint dp : dataSeries.getRawData()) {
            this.lines.add( new String[] {
                    dateFormat.format(dp.getDate()), String.valueOf(dp.getData())
            });
        }
    }
}
