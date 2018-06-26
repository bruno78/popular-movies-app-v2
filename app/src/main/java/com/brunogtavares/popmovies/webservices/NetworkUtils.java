package com.brunogtavares.popmovies.webservices;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.brunogtavares.popmovies.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by brunogtavares on 6/25/18.
 * Network Utils contains methods that takes care of most tasks
 * related to communications to Network
 * 1. checkForNetworkStatus check for network status returning a boolean
 * 2. createRequestUri creates Requests
 */

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String mApiKey = BuildConfig.MOVIE_API_KEY;
    private static final String mRequestUrl = "https://api.themoviedb.org/3/movie/";

    public static boolean checkForNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static String getJsonResponse(Uri.Builder uri) {
        URL url = NetworkUtils.creatUrl(uri.toString());

        String jsonResponse = null;

        try {
            jsonResponse = NetworkUtils.makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making HTTP request", e);
        }
        return jsonResponse;
    }

    public static URL creatUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Unable to create URL", e);
        }

        return url;
    }

    public static String makeHTTPRequest(URL url) throws IOException {
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
}
