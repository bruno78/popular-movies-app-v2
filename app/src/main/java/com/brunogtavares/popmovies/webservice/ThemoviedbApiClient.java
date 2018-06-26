package com.brunogtavares.popmovies.webservice;

import android.net.Uri;

import com.brunogtavares.popmovies.BuildConfig;
import com.brunogtavares.popmovies.model.Movie;
import com.brunogtavares.popmovies.model.MovieReview;
import com.brunogtavares.popmovies.model.MovieTrailer;

import java.util.List;

/**
 * Created by brunogtavares on 6/25/18.
 */

public class ThemoviedbApiClient {

    private static final String LOG_TAG = ThemoviedbApiClient.class.getSimpleName();

    private static final String MOVIE_API_KEY = BuildConfig.MOVIE_API_KEY;
    private static final String REQUEST_URL = "https://api.themoviedb.org/3/movie/";

    public static List<Movie> getAllMovies(String sortBy) {
        Uri.Builder uri = createMovieRequestUri(sortBy);
        String jsonResponse = NetworkUtils.getJsonResponse(uri);
        return ThemoviedbApiUtils.extractMovieFeatureFromJSON(jsonResponse);
    }

    public static List<MovieReview> getMovieReviews(int movieId, int limit) {
        Uri baseUri = Uri.parse(REQUEST_URL + movieId + "/reviews");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api_key", MOVIE_API_KEY);

        String jsonResponse = NetworkUtils.getJsonResponse(uriBuilder);
        return ThemoviedbApiUtils.extractMovieReviewFeatureFromJSON(jsonResponse, limit);
    }

    public static List<MovieTrailer> getMovietrailers(int movieId, int limit) {
        Uri baseUri = Uri.parse(REQUEST_URL + movieId + "/reviews");
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("api_key", MOVIE_API_KEY);

        String jsonResponse = NetworkUtils.getJsonResponse(uriBuilder);
        return ThemoviedbApiUtils.extractMovieTrailerFeatureFromJSON(jsonResponse, limit);
    }

    public static Uri.Builder createMovieRequestUri(String sortBy) {

        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath(sortBy);
        uriBuilder.appendQueryParameter("api_key", MOVIE_API_KEY);

        return uriBuilder;
    }

}
