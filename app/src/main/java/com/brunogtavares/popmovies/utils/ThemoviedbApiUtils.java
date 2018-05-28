package com.brunogtavares.popmovies.utils;

import android.text.TextUtils;
import android.util.Log;

import com.brunogtavares.popmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunogtavares on 5/10/18.
 * Themoviedb API Utils takes care of making HTTP request and extracting data from API using helper methods:
 * 1. createUrl takes a Url string and converts into URL object
 * 2. makeHTTPRequest takes url and makes a network request
 * 3. readFromInputStream if connection is successful, it takes the results as an input stream and returns
 * an output as JSON string
 * 4. extratFeaturefromJSON takes the results as JSON string and parse it returning a list of movies and
 * its features.
 */
public class ThemoviedbApiUtils {

    private static final String LOG_TAG = ThemoviedbApiUtils.class.getSimpleName();

    private ThemoviedbApiUtils() {
    }

    public static List<Movie> extractMovies(String urlString) {

        URL url = createUrl(urlString);
        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making HTTP request", e);
        }

        return extractFeatureFromJSON(jsonResponse);
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Unable to create URL", e);
        }

        return url;
    }

    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromInputStream(inputStream);

            }
            else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving movies JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromInputStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if(inputStream != null) {
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

    private static List<Movie> extractFeatureFromJSON(String movieJSON) {

        if(TextUtils.isEmpty(movieJSON)) return null;

        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject response = new JSONObject(movieJSON);
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject movieJson = results.getJSONObject(i);

                int id = movieJson.getInt("id");
                String title = movieJson.getString("original_title");
                String posterPath = buildImagePath(movieJson.getString("poster_path"));
                String backDropPath = buildImagePath(movieJson.getString("backdrop_path"));
                String synopsis = movieJson.getString("overview");
                double rating = movieJson.getDouble("vote_average");
                String releaseDate = movieJson.getString("release_date");

                movies.add(new Movie(id, title, posterPath, backDropPath, synopsis, rating, releaseDate));

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to parse movie JSON response", e);
        }

        return movies;
    }

    private static String buildImagePath(String moviePath) {
        // w185 for small devices
        // w500 for tablet
        return "http://image.tmdb.org/t/p/" + "w500/" + moviePath;
    }

}
