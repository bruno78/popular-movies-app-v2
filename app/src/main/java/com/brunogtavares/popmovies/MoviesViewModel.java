package com.brunogtavares.popmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.brunogtavares.popmovies.database.AppDatabase;
import com.brunogtavares.popmovies.model.Movie;

import java.util.List;

/**
 * Created by brunogtavares on 6/24/18.
 */

public class MoviesViewModel extends AndroidViewModel {

    private static final String MOVIES_REQUEST_URL = "https://api.themoviedb.org/3/movie/";

    private LiveData<List<Movie>> mMoviesDb;
    private List<Movie> mMovies;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        mMoviesDb = database.movieDao().getAllMovies();
    }

    public LiveData<List<Movie>> getMoviesDb() {
        return mMoviesDb;
    }
}
