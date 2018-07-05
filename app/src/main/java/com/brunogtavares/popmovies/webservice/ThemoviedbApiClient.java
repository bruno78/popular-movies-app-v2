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
    private static final String YOUTUBE_REQUEST_URL = "https://www.youtube.com/watch?v=";
    private static final String TRAILER_THUMBNAIL_REQUEST_URL = "https://img.youtube.com/vi/";
    private static final String TRAILER_THUMBNAIL = "sddefault.jpg";

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

    public static List<MovieTrailer> getMovieTrailers(int movieId, int limit) {
        Uri baseUri = Uri.parse(REQUEST_URL + movieId + "/videos");
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

    public static String getTrailerThumbnail(String key) {
        // Path to be built:
        // https://img.youtube.com/vi/<insert-youtube-video-id-here>/sddefault.jpg

        Uri baseUri = Uri.parse(TRAILER_THUMBNAIL_REQUEST_URL + key);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendPath(TRAILER_THUMBNAIL);
        uriBuilder.build();

        return uriBuilder.toString();
    }


    public static String getYoutubeTrailerPath(String key) {
        Uri baseUri = Uri.parse(YOUTUBE_REQUEST_URL + key);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.build();

        return uriBuilder.toString();
    }

}
