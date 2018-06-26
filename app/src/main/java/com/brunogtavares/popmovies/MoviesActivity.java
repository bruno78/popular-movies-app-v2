package com.brunogtavares.popmovies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
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

import com.brunogtavares.popmovies.database.MovieDatabase;
import com.brunogtavares.popmovies.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.brunogtavares.popmovies.utils.NetworkUtils.checkForNetworkStatus;

public class MoviesActivity extends AppCompatActivity
    implements MovieAdapter.MovieAdapterOnClickHandler,
    LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String LOG_TAG = MoviesActivity.class.getName();
    private static final String MOVIE_BUNDLE_KEY = "MOVIE_KEY";
    private static final String MOVIES_REQUEST_URL = "https://api.themoviedb.org/3/movie/";
    private static final int MOVIE_LOADER_ID = 1;
    private static final String LIST_STATE_KEY = "state_key";
    private static long mCurrentVisiblePosition = 0;

    private MovieAdapter mMovieAdapter;

    @BindView(R.id.rv_movie_list) RecyclerView mRecyclerView;
    @BindView(R.id.tv_empty_view) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private MovieDatabase mDb;

    private MoviesViewModel mMoviesViewModel;

    private List<Movie> mMovieListApi;
    private List<Movie> mMovieListDb;

    GridLayoutManager mGridLayoutManager;
    Parcelable mListState;


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

        // mMoviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        mDb = MovieDatabase.getInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMovieListDb = mDb.movieDao().getAllMovies().getValue();
            }
        });


        populateMovieList();

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
//        if(mMovieListDb.contains(movie)) {
//            int position = mMovieListDb.indexOf(movie);
//            setMovieToIntent(mMovieListDb.get(position));
//        }
//        else {
//            setMovieToIntent(movie);
//        }
        setMovieToIntent(movie);

    }

    private void setMovieToIntent(Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MOVIE_BUNDLE_KEY, movie);
        startActivity(intent);
    }


   // private void populateMovieList() {

        // Before populating the list, check for the network status
//        boolean isConnected = NetworkUtils.checkForNetworkStatus(this);
//        // If it's connected it will call the load manager otherwise will display no connection message
//        if (isConnected) {
//            // Start Loader Manager
            // getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
//
//            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//            String orderBy = sharedPrefs.getString(
//                getString(R.string.settings_order_by_key),
//                getString(R.string.settings_order_by_default));
//
//
//            mMovieListApi = mMoviesViewModel.getAllMoviesSortedBy(orderBy);
//            // If movies is not empty or null populate the adapter
//            if(mMovieListApi != null || !mMovieListApi.isEmpty()) {
//                mMovieAdapter.setMovieList(mMovieListApi);
//                mRecyclerView.setAdapter(mMovieAdapter);
//            }
//            else {
//                // Set empty state text to display "No movies found."
//                mErrorMessageDisplay.setText(R.string.no_movies);
//            }
//
//
//
//        }
//        else {
//            mLoadingIndicator.setVisibility(View.GONE);
//            // Update empty state with no connection error message
//            mErrorMessageDisplay.setText(R.string.no_connection);
//        }


        // Before populating the list, check for the network status
        // boolean isConnected = NetworkUtils.checkForNetworkStatus(this);

    private void populateMovieList() {

        // Before populating the list, check for the network status
        boolean isConnected = checkForNetworkStatus(this);
        // If it's connected it will call the load manager otherwise will display no connection message
        if (isConnected) {
            // Start Loader Manager
            getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
        }
        else {
            mLoadingIndicator.setVisibility(View.GONE);
            // Update empty state with no connection error message
            mErrorMessageDisplay.setText(R.string.no_connection);
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

        // Uri.Builder uri = NetworkUtils.createRequestUri(orderBy);

        // return new MoviesLoader(this, uri.toString());
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {

        // set the invisibility of the Progress bar to invisible
        mLoadingIndicator.setVisibility(View.GONE);

        // Clear the adapter from previous data
        resetAdapter();

        // If movies is not empty or null populate the adapter
        if(!movies.isEmpty()) {
            mMovieAdapter.setMovieList(movies);
            mRecyclerView.setAdapter(mMovieAdapter);
        }
        else {
            // Set empty state text to display "No movies found."
            mErrorMessageDisplay.setText(R.string.no_movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        resetAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ((GridLayoutManager) mRecyclerView.getLayoutManager())
//                .scrollToPosition((int) mCurrentVisiblePosition);
//        mCurrentVisiblePosition = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mCurrentVisiblePosition = ((GridLayoutManager) mRecyclerView.getLayoutManager())
//                .findFirstVisibleItemPosition();
        // resetAdapter();
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(LIST_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());
//        return bundle;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle state) {
//        // Retrieve list state and list/item positions
//        if(state instanceof Bundle) {
//            mListState = ((Bundle) state).getParcelable(LIST_STATE_KEY);
//        }
//        super.onRestoreInstanceState(state);
//    }

}
