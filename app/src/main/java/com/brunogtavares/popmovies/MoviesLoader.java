package com.brunogtavares.popmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.brunogtavares.popmovies.webservice.ThemoviedbApiClient;
import com.brunogtavares.popmovies.model.Movie;

import java.util.List;

/**
 * Created by brunogtavares on 5/19/18.
 *  Loaders handle asynchronous tasks, it will help restoring data on configuration changes, but also,
 *  this avoids issues present in the AsyncTask like potential crashes when rotating the device
 *  when the task is still running.
 */

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {

    private String mSortByParam;

    public MoviesLoader(Context context, String sortByParam) {
        super(context);
        mSortByParam = sortByParam;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        return ThemoviedbApiClient.getAllMovies(mSortByParam);
    }
}
