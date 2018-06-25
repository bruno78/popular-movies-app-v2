package com.brunogtavares.popmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.brunogtavares.popmovies.BuildConfig;

/**
 * Created by brunogtavares on 6/25/18.
 */

public class NetworkUtils {

    private static final String mApiKey = BuildConfig.MOVIE_API_KEY;
    private static final String mRequestUrl = "https://api.themoviedb.org/3/movie/";

    public static boolean checkForNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static Uri.Builder createRequestUri(String sortBy) {

        Uri baseUri = Uri.parse(mRequestUrl);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath(sortBy);
        uriBuilder.appendQueryParameter("api_key", mApiKey);

        return uriBuilder;
    }
}
