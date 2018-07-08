package com.brunogtavares.popmovies.MovieReviews;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.brunogtavares.popmovies.model.MovieReview;
import com.brunogtavares.popmovies.webservice.ThemoviedbApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunogtavares on 5/19/18.
 *  Loaders handle asynchronous tasks, it will help restoring data on configuration changes, but also,
 *  this avoids issues present in the AsyncTask like potential crashes when rotating the device
 *  when the task is still running.
 */

public class MovieReviewsLoader extends AsyncTaskLoader<List<MovieReview>> {

    List<MovieReview> mMovieReviews;
    int mMovieId, mLimit;

    public MovieReviewsLoader(Context context, int movieId, int limit) {
        super(context);
        mMovieReviews = new ArrayList<>();
        this.mMovieId = movieId;
        this.mLimit = limit;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MovieReview> loadInBackground() {

        mMovieReviews =
                ThemoviedbApiClient.getMovieReviews(mMovieId, mLimit);
        return mMovieReviews;
    }
}
