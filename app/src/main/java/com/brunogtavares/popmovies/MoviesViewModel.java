package com.brunogtavares.popmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.brunogtavares.popmovies.model.Movie;

import java.util.List;

/**
 * Created by brunogtavares on 6/24/18.
 */

public class MoviesViewModel extends AndroidViewModel {

    private final MovieRepository mMovieRepository;

    private LiveData<List<Movie>> mAllFavoriteMovies;

    private List<Movie> mAllMoviesApi;

    public MoviesViewModel(@NonNull Application application) {
        super(application);

        mMovieRepository = new MovieRepository(application);

        mAllFavoriteMovies = mMovieRepository.getAllFavoriteMovies();

        mAllMoviesApi = mMovieRepository.getAllPopularMovies();
    }

    public LiveData<List<Movie>> getAllFavoriteMovies() {
        return mAllFavoriteMovies;
    }

    public void insertFavoriteMovie(Movie movie) {
        mMovieRepository.insertFavoriteMovieFromDb(movie);
    }

    public void deleteFavoriteMovie(Movie movie) {
        mMovieRepository.deleteFavoriteMovieFromDb(movie);
    }

    public List<Movie> getAllMoviesApi() {
        return mAllMoviesApi;
    }

    public List<Movie> getAllMoviesSortedBy(String sort) {
        return mMovieRepository.getAllMoviesApiBySort(sort);
    }
}
