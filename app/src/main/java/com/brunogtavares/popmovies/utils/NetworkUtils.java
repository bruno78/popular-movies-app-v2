package com.brunogtavares.popmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.brunogtavares.popmovies.BuildConfig;

public class NetworkUtils {

    public static boolean checkForNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static Uri.Builder createRequestUri(String sortBy, String url) {

        String apiKey = BuildConfig.MOVIE_API_KEY;

        Uri baseUri = Uri.parse(url);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath(sortBy);
        uriBuilder.appendQueryParameter("api_key", apiKey);

        return uriBuilder;
    }


}
