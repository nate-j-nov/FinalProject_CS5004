import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Class file for generating a csv file from a data series
 */
public class DataSeriesCsvGenerator extends CsvGeneratorService {
    private ArrayList<DataSeries> dataSeriesList;
    private SimpleDateFormat dateFormat;
    private boolean moreThanOne;

    /**
     * Argument constructor
     * @param dataSeriesList ArrayList of data series to generate the CSV
     */
    public DataSeriesCsvGenerator(ArrayList<DataSeries> dataSeriesList) {
        super();
        this.dataSeriesList = new ArrayList<>();
        this.dataSeriesList.addAll(dataSeriesList);
        this.dateFormat = new SimpleDateFormat("MM-yyyy");
        moreThanOne = dataSeriesList.size() > 1;
    }

    @Override
    public String generateCsvFile() {
        Date now = new Date();
        // Create the file name
        String fileName;
        if(moreThanOne) {
            fileName = dataSeriesList.get(0).getSeriesCode()
                    + "+"
                    + dataSeriesList.get(1).getSeriesCode()
                    + "_"
                    + dateFormat.format(now)
                    + ".csv";
        } else {
            fileName = dataSeriesList.get(0).getSeriesCode()
                    + "_"
                    + dateFormat.format(now)
                    + ".csv";
        }

        // Create the lines from the dataset
        generateCsvLines();

        try {
            FileWriter writer = new FileWriter(fileName);

            // Set the header information for the file
            writer.append("SeriesName_1: ")
                    .append(dataSeriesList.get(0).getSeriesName())
                    .append(",Series Code_2: ")
                    .append(dataSeriesList.get(0).getSeriesCode());
            if(moreThanOne) {
                writer.append(",SeriesName_2: " )
                        .append(dataSeriesList.get(1).getSeriesName())
                        .append(",SeriesCode_2: ")
                        .append(dataSeriesList.get(1).getSeriesCode());
            }
            writer.append("\n");

            // Set the column headings
            writer.append("Date_1,").append(dataSeriesList.get(0).getSeriesName());
            if(moreThanOne) writer.append(",Date_2,").append(dataSeriesList.get(1).getSeriesName());
            writer.append("\n");

            // Generate the actual lines of the CSV file
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

    /**
     * Essentially, this ensures that all data points of each dataset is written to the CSV file
     */
    @Override
    protected void generateCsvLines() {
        if(!moreThanOne) {
            for(DataPoint dp : dataSeriesList.get(0).getRawData()) {
                this.lines.add( new String[] {
                        dateFormat.format(dp.getDate()), String.valueOf(dp.getData())
                });
            }
        } else  {
            DataSeries longerDataSeries;
            DataSeries shorterDataSeries;

            if(dataSeriesList.get(1).getRawData().size() > dataSeriesList.get(0).getRawData().size()) {
                longerDataSeries = dataSeriesList.get(1);
                shorterDataSeries = dataSeriesList.get(0);
            } else {
                longerDataSeries = dataSeriesList.get(0);
                shorterDataSeries = dataSeriesList.get(1);
            }

            for(int i = 0; i < longerDataSeries.getRawData().size() - 1; i++) {
                if(i < shorterDataSeries.getRawData().size() - 1) {
                    this.lines.add(new String[] {
                            dateFormat.format(dataSeriesList.get(0).getRawData().get(i).getDate()),
                            String.valueOf(dataSeriesList.get(0).getRawData().get(i).getData()),
                            dateFormat.format(dataSeriesList.get(1).getRawData().get(i).getDate()),
                            String.valueOf(dataSeriesList.get(1).getRawData().get(i).getData())
                    });
                }
                else {
                    if(shorterDataSeries == dataSeriesList.get(0)) {
                        this.lines.add(new String[] {
                                "",
                                "",
                                dateFormat.format(dataSeriesList.get(1).getRawData().get(i).getDate()),
                                String.valueOf(dataSeriesList.get(1).getRawData().get(i).getData())
                        });

                    } else {
                        this.lines.add(new String[] {
                                dateFormat.format(dataSeriesList.get(0).getRawData().get(i).getDate()),
                                String.valueOf(dataSeriesList.get(0).getRawData().get(i).getData()),
                                "",
                                ""
                        });
                    }
                }
            }

        }
    }
}
