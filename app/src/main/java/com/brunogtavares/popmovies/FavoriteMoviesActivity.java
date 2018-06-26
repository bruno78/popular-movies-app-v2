package com.brunogtavares.popmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brunogtavares.popmovies.database.MovieDatabase;
import com.brunogtavares.popmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brunogtavares on 6/23/18.
 */

public class FavoriteMoviesActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler
       // implements LoaderManager.LoaderCallbacks<List<Movie>>, MovieAdapter.MovieAdapterOnClickHandler {
{
    private static final String LOG_TAG = MoviesActivity.class.getName();
    private static final String MOVIE_BUNDLE_KEY = "MOVIE_KEY";
    private static final String MOVIES_REQUEST_URL = "https://api.themoviedb.org/3/movie/";
    private static final int MOVIE_LOADER_ID = 1;

    private MovieAdapter mMovieAdapter;

    @BindView(R.id.rv_movie_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_view)
    TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    private MovieDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);

        // Creating GridLayout to populate the movies as grid
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        // This will help to cache the viewHolders and improve scrolling performance
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Create a new adapter that takes an empty list of movies as input
        mMovieAdapter = new MovieAdapter( this);
        mMovieAdapter.setContext(getApplicationContext());
        mMovieAdapter.setMovieList(new ArrayList<Movie>());


        // Set the adapter on the RecyclerView
        // so the list can be populated in the user interface
        mRecyclerView.setAdapter(mMovieAdapter);

        // populateMovieList();

        mDb = MovieDatabase.getInstance(getApplicationContext());
        retrieveMovies();

    }

    private void retrieveMovies() {
        final LiveData<List<Movie>> favoriteMovies = mDb.movieDao().getAllMovies();
        favoriteMovies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieAdapter.setMovieList(movies);
            }
        });
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MOVIE_BUNDLE_KEY, movie);
        startActivity(intent);
    }

}