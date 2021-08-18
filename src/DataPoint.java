import java.util.Date;

/**
 * @author Nate Novak
 * 7/26/2021
 * Data Point class to represent data retrieved from the API.
 */
public class DataPoint {
    private final Date date;
    private final double data;

    /**
     * Argument constructor for a datapoint
     * @param date Date representation of the date for the data point
     * @param data double representation of the data for the data point
     */
    public DataPoint(Date date, Double data) {
        this.date = date;
        this.data = data;
    }

    /**
     * Gets the date of the data point
     * @return Date representation of the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the data of the data point
     * @return double representation of the data
     */
    public double getData() {
        return data;
    }
}
