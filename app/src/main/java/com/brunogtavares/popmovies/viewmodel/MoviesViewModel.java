package com.brunogtavares.popmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.brunogtavares.popmovies.database.MovieDatabase;
import com.brunogtavares.popmovies.model.Movie;

import java.util.List;

/**
 * Created by brunogtavares on 6/24/18.
 */

public class MoviesViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> mAllFavoriteMovies;

    public MoviesViewModel(@NonNull Application application) {
        super(application);

        mAllFavoriteMovies = MovieDatabase.getInstance(application)
                .movieDao().getAllMovies();

    }

    public LiveData<List<Movie>> getAllFavoriteMovies() {
        return mAllFavoriteMovies;
    }
}
