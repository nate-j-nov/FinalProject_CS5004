import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class CsvGeneratorService {
    protected ArrayList<String[]> lines;

    public CsvGeneratorService() {
        this.lines = new ArrayList<>();
    }

    public abstract String generateCsvFile();

    protected String convertToCsvLine(String[] data) {
        return Stream.of(data).collect(Collectors.joining(","));
    }

    protected abstract void generateCsvLines();

}
