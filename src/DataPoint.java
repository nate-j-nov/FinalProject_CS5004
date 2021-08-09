
/**
 * @author Nate Novak
 * 7/26/2021
 * Data Point class to represent data retrieved from the API.
 */

import java.util.Date;

public class DataPoint {
    private final Date date;
    private final double data;

    public DataPoint(Date date, Double data) {
        this.date = date;
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public double getData() {
        return data;
    }
}
