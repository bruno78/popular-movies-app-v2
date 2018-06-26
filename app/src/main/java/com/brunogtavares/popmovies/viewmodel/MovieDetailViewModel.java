package com.brunogtavares.popmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.brunogtavares.popmovies.database.MovieDatabase;
import com.brunogtavares.popmovies.model.Movie;

public class MovieDetailViewModel extends ViewModel {

    private LiveData<Movie> mMovie;

    public MovieDetailViewModel(MovieDatabase database, int movieId) {
        mMovie = database.movieDao().getMovieById(movieId);
    }

    public LiveData<Movie> getmMovie() {
        return mMovie;
    }

}
