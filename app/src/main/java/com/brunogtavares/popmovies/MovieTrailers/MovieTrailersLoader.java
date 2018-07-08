package com.brunogtavares.popmovies.MovieTrailers;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.brunogtavares.popmovies.model.MovieTrailer;
import com.brunogtavares.popmovies.webservice.ThemoviedbApiClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunogtavares on 6/27/18.
 */

public class MovieTrailersLoader extends AsyncTaskLoader<List<MovieTrailer>> {
    private List<MovieTrailer> mMovieTrailers;
    int mMovieId, mLimit;


    public MovieTrailersLoader(Context context, int movieId, int limit) {
        super(context);
        mMovieTrailers = new ArrayList<>();
        this.mMovieId = movieId;
        this.mLimit = limit;
    }

    @Override
    public List<MovieTrailer> loadInBackground() {

        mMovieTrailers =
                ThemoviedbApiClient.getMovieTrailers(mMovieId, mLimit);
        return mMovieTrailers;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}