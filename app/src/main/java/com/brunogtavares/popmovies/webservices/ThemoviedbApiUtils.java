package com.brunogtavares.popmovies.webservices;

import android.text.TextUtils;
import android.util.Log;

import com.brunogtavares.popmovies.model.Movie;
import com.brunogtavares.popmovies.model.MovieReview;
import com.brunogtavares.popmovies.model.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brunogtavares on 5/10/18.
 * Themoviedb API Utils takes care of making HTTP request and extracting data from API using helper methods:
 * 1. createUrl takes a Url string and converts into URL object
 * 2. makeHTTPRequest takes url and makes a network request
 * 3. readFromInputStream if connection is successful, it takes the results as an input stream and returns
 * an output as JSON string
 * 4. extratFeaturefromJSON takes the results as JSON string and parse it returning a list of movies and
 * its features.
 */
public class ThemoviedbApiUtils {

    private static final String LOG_TAG = ThemoviedbApiUtils.class.getSimpleName();

    private ThemoviedbApiUtils() {
    }

    public static List<Movie> extractMovieFeatureFromJSON(String movieJSON) {

        List<Movie> movies = new ArrayList<>();

        if(TextUtils.isEmpty(movieJSON)) return movies;


        try {
            JSONObject response = new JSONObject(movieJSON);
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject movieJson = results.getJSONObject(i);

                int id = movieJson.getInt("id");
                String title = movieJson.getString("original_title");
                String posterPath = buildImagePath(movieJson.getString("poster_path"));
                String backDropPath = buildImagePath(movieJson.getString("backdrop_path"));
                String synopsis = movieJson.getString("overview");
                double rating = movieJson.getDouble("vote_average");
                String releaseDate = movieJson.getString("release_date");

                movies.add(new Movie(id, title, posterPath, backDropPath, synopsis, rating, releaseDate));

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to parse movie JSON response", e);
        }

        return movies;
    }

    private static String buildImagePath(String moviePath) {
        // w185 for small devices
        // w500 for tablet
        return "http://image.tmdb.org/t/p/" + "w500/" + moviePath;
    }

    public static List<MovieTrailer> extractMovieTrailerFeatureFromJSON(String movieTrailerJSON, int limit) {

        List<MovieTrailer> movieTrailers = new ArrayList<>();

        if(TextUtils.isEmpty(movieTrailerJSON)) {
            return movieTrailers;
        }

        try {
            JSONObject response = new JSONObject(movieTrailerJSON);
            JSONArray results = response.getJSONArray("results");

            // adjust the number of reviews
            if(limit == 0 || limit > results.length()) limit = results.length();

            for (int i = 0; i < limit; i++) {

                // Get the movie Id;
                int id = response.getInt("id");

                JSONObject movieTrailerJson = results.getJSONObject(i);
                String key = buildMovieTrailerPath(movieTrailerJson.getString("key"));

                movieTrailers.add(new MovieTrailer(key, id));

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to parse movieTrailer JSON response", e);
        }

        return movieTrailers;
    }

    private static String buildMovieTrailerPath(String movieTrailerPath) {
        return "https://www.youtube.com/watch?v=" + movieTrailerPath;
    }

    public static List<MovieReview> extractMovieReviewFeatureFromJSON(String movieReviewJSON, int limit) {

        List<MovieReview> movieReviews = new ArrayList<>();

        if(TextUtils.isEmpty(movieReviewJSON)) {
            return movieReviews;
        }

        try {
            JSONObject response = new JSONObject(movieReviewJSON);
            JSONArray results = response.getJSONArray("results");

            // adjust the number of reviews
            if(limit == 0 || limit > results.length()) limit = results.length();

            for (int i = 0; i < limit; i++) {

                // Get the movie Id;
                int id = response.getInt("id");

                JSONObject movieTrailerJson = results.getJSONObject(i);
                String author = movieTrailerJson.getString("author");
                String content = movieTrailerJson.getString("content");

                movieReviews.add(new MovieReview(id, author, content));

            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to parse movieTrailer JSON response", e);
        }

        return movieReviews;

    }

}
