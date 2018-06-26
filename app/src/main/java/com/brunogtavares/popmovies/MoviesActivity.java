package com.brunogtavares.popmovies;

import android.app.LoaderManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brunogtavares.popmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.brunogtavares.popmovies.webservices.NetworkUtils.checkForNetworkStatus;

public class MoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Movie>>, MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MoviesActivity.class.getName();

    private static final String MOVIE_BUNDLE_KEY = "MOVIE_KEY";

    private static final int MOVIE_LOADER_ID = 1;
    private static final String LIST_STATE_KEY = "state_key";
    private static Parcelable mListState;

    private GridLayoutManager mGridLayoutManager;
    private MovieAdapter mMovieAdapter;

    @BindView(R.id.rv_movie_list) RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_view) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private MoviesViewModel mMovieViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        ButterKnife.bind(this);

        // Creating GridLayout to populate the movies as grid
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
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

        populateMovieList();

        mMovieViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        mMovieViewModel.getAllFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                mMovieAdapter.setMovieList(movies);
            }
        });


    }

    // Creates the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Handles the options selected in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        if( id == R.id.action_favorites) {
            Intent favoritesIntent = new Intent(this, FavoriteMoviesActivity.class);
            startActivity(favoritesIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Handles when user click on menu poster
    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MOVIE_BUNDLE_KEY, movie);
        startActivity(intent);

    }

    private void populateMovieList() {

        // Before populating the list, check for the network status
        boolean isConnected = checkForNetworkStatus(this);
        // If it's connected it will call the load manager otherwise will display no connection message
        if (isConnected) {
            // Start Loader Manager
            getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }
        else {
            showErrorMessage();
        }

    }


    private void resetAdapter() {
        // Create a new adapter with an empty movie list
        mMovieAdapter.setMovieList(new ArrayList<Movie>());
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        return new MoviesLoader(this, orderBy);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {

        // Clear the adapter from previous data
        resetAdapter();

        // If movies is not empty or null populate the adapter
        if(movies == null) {
            showErrorMessage();
        }
        else if (movies.size() == 0) {
            showEmptyState();
        }
        else {
            mLoadingIndicator.setVisibility(View.GONE);
            mMovieAdapter.setMovieList(movies);
            mRecyclerView.setAdapter(mMovieAdapter);
        }
    }

    private void showEmptyState() {
        mLoadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No movies found."
        mErrorMessageDisplay.setText(R.string.no_movies);
    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.GONE);
        // Update empty state with no connection error message
        mErrorMessageDisplay.setText(R.string.no_connection);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        resetAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resetAdapter();
    }


}
