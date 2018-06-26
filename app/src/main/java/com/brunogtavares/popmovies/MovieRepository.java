package com.brunogtavares.popmovies;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.brunogtavares.popmovies.database.MovieDatabase;
import com.brunogtavares.popmovies.database.MovieDao;
import com.brunogtavares.popmovies.model.Movie;
import com.brunogtavares.popmovies.utils.NetworkUtils;
import com.brunogtavares.popmovies.utils.ThemoviedbApiUtils;

import java.util.List;

/**
 * Created by brunogtavares on 6/25/18.
 */

public class MovieRepository {

    private static final String TAG = MovieRepository.class.getSimpleName();

    private MovieDao mMovieDao;
    private LiveData<List<Movie>> mAllFavoriteMovies;
    private LiveData<Movie> mFavoriteMovie;
    private List<Movie> mAllMoviesApi;

    public MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getsInstance(application);
        mMovieDao = db.movieDao();
        mAllFavoriteMovies = mMovieDao.getAllMovies();
    }

    public LiveData<List<Movie>> getAllFavoriteMovies() {
        return mAllFavoriteMovies;
    }

    public LiveData<Movie> getFavoriteMovieById(int movieId) {
        return mMovieDao.getMovieById(movieId);
    }

    public void insertFavoriteMovieFromDb(Movie movie) {
        new insertAsyncTask(mMovieDao).execute(movie);
    }

    public void deleteFavoriteMovieFromDb(Movie movie) {
        new deleteAsyncTask(mMovieDao).execute(movie);
    }

    public List<Movie> getAllMoviesApiBySort(String url) {
        String[] urls = {url};
        return new getAllApiMoviesAsyncTask().doInBackground(urls[0]);
    }

    public List<Movie> getAllPopularMovies() {
        String[] popular = {"popular"};
        return new getAllApiMoviesAsyncTask().doInBackground(popular[0]);
    }

    public List<Movie> getAllTopRatedMovies() {
        String[] topRated = {"top_rated"};
        return new getAllApiMoviesAsyncTask().doInBackground(topRated[0]);
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private final MovieDao mAsyncMovieDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncMovieDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... movies) {
            mAsyncMovieDao.insert(movies[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Movie, Void, Void> {

        private final MovieDao mAsyncMovieDao;

        deleteAsyncTask(MovieDao dao) {
            mAsyncMovieDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... movies) {
            mAsyncMovieDao.delete(movies[0]);
            return null;
        }
    }

    private static class getAllApiMoviesAsyncTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(String... urls) {
            String url = NetworkUtils.createRequestUri(urls[0]).toString();
            return ThemoviedbApiUtils.extractMovies(url);
        }
    }

}
