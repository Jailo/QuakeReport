package com.example.android.quakereport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.net.HttpURLConnection;
import java.util.List;


/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Tag for the Log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {
        // Create new URL object
        URL url = createUrl(requestUrl);

        // Create a null String for the JSON response
        String jsonResponse = null;

        // Perform HTTP request and get JSON response back
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making HTTP request: " + e);
        }

        // Extract relevant fields from JSON response and create a list of {@link Earthquake} objects
        List<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);

        // Return list of {@link Earthquake}s
        return earthquakes;
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Earthquake> extractFeatureFromJson(String earthquakeJSON) {

        //if JSON response is empty or null, return early
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the jsonResponse. If there's a problem with making the http request from
        // the given url, a IOException exception object will be thrown.
        // Or, If there's a problem with the way the JSON is formatted,
        // a JSONException exception object will be thrown.
        // Catch both the exceptions so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by jsonResponse string and
            // build up a list of Earthquake objects with the corresponding data.

            //Convert jsonResponse String into a JSONObject
            JSONObject jsonObjectString = new JSONObject(earthquakeJSON);

            // Extract “features” JSONArray
            JSONArray earthquakeArray = jsonObjectString.getJSONArray("features");

            //  Loop through each feature in the array
            for (int i = 0; i < earthquakeArray.length(); i++) {
                //Get earthquake JSONObject at position i
                JSONObject currentEarthquakeJSONObject = earthquakeArray.getJSONObject(i);

                // Get “properties” JSONObject
                JSONObject properties = currentEarthquakeJSONObject.getJSONObject("properties");

                //Extract “mag” for magnitude
                Double magnitude = properties.getDouble("mag");

                //Extract “place” for location
                String location = properties.getString("place");

                //Extract “time” for time
                Long time = properties.getLong("time");

                // Extract "url" for the url
                String url = properties.getString("url");

                //Create Earthquake java object from magnitude, location, and time
                Earthquake newEarthquake = new Earthquake(magnitude, location, time, url);

                //Add earthquake to list of earthquakes
                earthquakes.add(newEarthquake);

            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }


    /**
     * Create a URL object from the given string URL
     */
    private static URL createUrl(String stringUrl) {

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error creating URL: ", e);
        }

        return url;
    }

    /**
     * Make an HTTP Request to the given URL and return the response as a String
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If url is null return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* Milliseconds */);
            urlConnection.setConnectTimeout(15000 /* Milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (Response code 200)
            // Read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                // Else Log the error response code
                Log.e(LOG_TAG, "Error Response code is: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error creating HTTP Request: ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies that an IOException
                // could be thrown.
                inputStream.close();
            }
        }

        return jsonResponse;
    }


    /**
     * Convert InputStream into a String that returns the entire JSON response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
