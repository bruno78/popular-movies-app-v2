package com.brunogtavares.popmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.brunogtavares.popmovies.database.AppDatabase;
import com.brunogtavares.popmovies.model.Movie;

public class MovieDetailViewModel extends ViewModel {

    private LiveData<Movie> mMovie;

    public MovieDetailViewModel(AppDatabase database, int movieId) {
        mMovie = database.movieDao().getMovieById(movieId);
    }

    public LiveData<Movie> getmMovie() {
        return mMovie;
    }

}
