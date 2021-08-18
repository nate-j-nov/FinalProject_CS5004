import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/*
I followed this article for guidance:
https://medium.com/swlh/getting-json-data-from-a-restful-api-using-java-b327aafb3751
 */

/**
 * @author Nate Novak
 * CS5004 Summer 2021
 * Class file for an API handler, which is a general service to make API requests.
 */
class ApiHandler {

    private String baseUrlString;
    private HttpURLConnection connection;

    /**
     * Argument constructor taking a string to create the base URL
     * @param baseUrlString base URL string for accessing the API
     */
    public ApiHandler(String baseUrlString) {
        this.baseUrlString = baseUrlString;
    }

    /**
     * Make a request to the API
     * @param restOfUrlString endpoint for the Web API
     * @return JSONObject representation of the data.
     */
    public JSONObject makeRequest(String restOfUrlString) {
        String connectionUrlString = baseUrlString + restOfUrlString;
        try {
            URL connectionUrl = new URL(connectionUrlString);
            connection = (HttpURLConnection) connectionUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode != 200) {
                throw new RuntimeException("Connection Unsuccessful. Response Code: " + responseCode);
            } else {
                String inline = "";
                Scanner scanner = new Scanner(connectionUrl.openStream());

                while(scanner.hasNext()) {
                    inline += scanner.nextLine();
                }
                scanner.close();

                JSONParser parser = new JSONParser();

                JSONObject data = (JSONObject) parser.parse(inline);
                connection.disconnect();
                return data;
            }
        } catch(MalformedURLException mue) {
            System.out.println("Malformed URL");
        } catch (IOException e) {
            System.out.println("Could not open connection");
        } catch (ParseException e) {
            System.out.println("Could not parse the data");
        }

        return null;
    }
}