package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ZipCodeClient {
    private static final String HOST = "https://www.zipcodeapi.com/rest/";
    private static final String PATH = "/radius.json";
    private static final String DEFAULT_UNITS = "mile";
    private static final Integer DEFAULT_RADIUS = 50;
    private static final String API_KEY = "lMeZObhVGQKr6uz0UEypVoXKPEENxG4g0TTfwcKTPj7C2iTAKm7MpgKQsxa22shU";

    /**
     * Return the zip codes within the given radius.
     * @param zipcode
     * @param radius
     * @param unit
     * @return list of zip codes
     */
    public List<String> searchZipCodeByRadius(String zipcode, Integer radius, String unit) {
        if (radius == null) {
            radius = DEFAULT_RADIUS;
        }
        if (unit == null) {
            unit = DEFAULT_UNITS;
        }
        // https://www.zipcodeapi.com/rest/ErZhj2QacmIEophZFe7zdZrOyr0YXQg8v32uRLwmmTNAxReLgrrWJJI2YBQmJCaL/radius.json/15213/20/km
        String query = String.format("/%s/%s/%s", zipcode, radius.toString(), unit);
        String url = new StringBuilder().append(HOST).append(API_KEY).append(PATH).append(query).toString();
        StringBuilder responseBody = new StringBuilder();

        try {
            // Create a URLConnection instance that represents a connection to the remote
            // object referred to by the URL. The HttpUrlConnection class allows us to
            // perform basic HTTP requests without the use of any additional libraries.
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Get the status code from an HTTP response message. To execute the request we
            // can use the getResponseCode(), connect(), getInputStream() or
            // getOutputStream() methods.
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return new ArrayList<>();
            }

            // Create a BufferedReader to help read text from a character-input stream.
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseBody.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // Extract zip codes.
            JSONObject obj = new JSONObject(responseBody.toString());
            if (!obj.isNull("zip_codes")) {
                JSONArray zipcodes = obj.getJSONArray("zip_codes");
                return getZipCodes(zipcodes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    /**
     * Format the JSONArray to list of zip codes
     * @param zipcodes
     * @return
     * @throws JSONException
     */
    private List<String> getZipCodes(JSONArray zipcodes) throws JSONException {
        List<String> zipCodeList = new ArrayList<>();
        for (int i = 0; i < zipcodes.length(); i++) {
            JSONObject address = zipcodes.getJSONObject(i);
            zipCodeList.add(address.getString("zip_code"));
        }
        return zipCodeList;
    }

    public static void main(String[] args) {
        ZipCodeClient client = new ZipCodeClient();
        List<String> zipcodes = client.searchZipCodeByRadius("15232", 20, "km");
        for (String str : zipcodes) {
            System.out.println(str);
        }
    }
}