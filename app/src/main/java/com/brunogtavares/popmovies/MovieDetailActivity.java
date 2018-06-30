package com.brunogtavares.popmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.brunogtavares.popmovies.adapter.MovieReviewAdapter;
import com.brunogtavares.popmovies.adapter.MovieTrailerAdapter;
import com.brunogtavares.popmovies.database.MovieDatabase;
import com.brunogtavares.popmovies.loader.MovieReviewsLoader;
import com.brunogtavares.popmovies.loader.MovieTrailersLoader;
import com.brunogtavares.popmovies.model.Movie;
import com.brunogtavares.popmovies.model.MovieReview;
import com.brunogtavares.popmovies.model.MovieTrailer;
import com.brunogtavares.popmovies.viewmodel.MovieDetailViewModel;
import com.brunogtavares.popmovies.viewmodel.MovieDetailViewModelFactory;
import com.brunogtavares.popmovies.webservice.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * MovieDetailActivity displays detail from each movie.
 * It users two loaders one for the trailers and another from
 * reviews. More info on loaders in the same activity:
 * https://stackoverflow.com/questions/15643907/multiple-loaders-in-same-activity
 */
public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieTrailer>>, MovieTrailerAdapter.MovieTrailerOnClickHandler {

    private static final String LOG_TAG = MovieDetailActivity.class.getSimpleName();

    // Extra for the movie received from the intent
    protected static final String MOVIE_BUNDLE_KEY = "MOVIE_KEY";
    private static final int MOVIE_TRAILERS_LOADER_ID = 101;
    private static final int MOVIE_REVIEWS_LOADER_ID = 102;

    // Extra for the movie received from after rotation
    private static final String MOVIE_STATE_KEY = "MOVIE_STATE_KEY";
    private static final String TRAILER_RV_STATE_KEY = "TRAILER_RV_STATE";
    private static final String REVIEW_RV_STATE_KEY = "REVIEW_RV_STATE";
    private static final String SCROLL_POSITION_KEY = "SCROLL_POSITION_KEY";

    // Constant for default task when movie is not in Favorites.
    private static final int DEFAULT_MOVIE_ID = -1;
    private static final String MOVIE_INSTANCE_KEY = "MOVIE_INSTANCE_KEY";
    private int mMovieId = DEFAULT_MOVIE_ID;

    private Boolean mMovieState;
    private static Parcelable mTrailerRvState, mReviewRVState;
    private static int[] scrollPositions;

    // This number will be adjustable for shared preferences later
    private static final int MOVIE_TRAILER_LIMIT = 3;
    private static final int MOVIE_REVIEW_LIMIT = 3;

    private RecyclerView.LayoutManager mMovieTrailerRVLayoutManager, mMovieReviewRVLayoutManager;
    private MovieTrailerAdapter mMovieTrailerAdapter;
    private MovieReviewAdapter mMovieReviewAdapter;

    private static LoaderManager.LoaderCallbacks<List<MovieReview>> mReviewsLoaderListener;

    @BindView(R.id.iv_movie_backdrop) ImageView mBackdrop;
    @BindView(R.id.iv_movie_poster_thumbnail) ImageView mPosterThumbnail;
    @BindView(R.id.tv_rating) TextView mMovieRating;
    @BindView(R.id.tv_date_released) TextView mDateReleased;
    @BindView(R.id.tv_synopsis) TextView mSinopsys;
    @BindView(R.id.fab_favorites) FloatingActionButton mAddFavoritesButton;
    @BindView(R.id.rv_movie_trailer) RecyclerView mMovieTrailerRecyclerView;
    @BindView(R.id.rv_movie_review) RecyclerView mMovieReviewRecyclerView;
    @BindView(R.id.sv_movie_detail) ScrollView mScrollView;
    @BindView(R.id.ll_reviews_layout) LinearLayout mReviewsLayout;
    @BindView(R.id.ll_trailers_layout) LinearLayout mTrailersLayout;

    private Movie mMovie;

    // Database
    private MovieDatabase mDb;

    private AppExecutors mTask;
    private MovieDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        mTask = AppExecutors.getInstance();
        mDb = MovieDatabase.getInstance(getApplicationContext());

        if(savedInstanceState != null) {
            // Get movie from previous state
            mMovie = savedInstanceState.getParcelable(MOVIE_BUNDLE_KEY);
            mMovieState = savedInstanceState.getBoolean(MOVIE_STATE_KEY);
        }
        else {
            // Get movie from intent
            mMovie = getIntent().getParcelableExtra(MOVIE_BUNDLE_KEY);
            mMovieState = false;
        }


        initViewModel();

        setColorFavoriteButton();

        populateMovieUI();

        // Load Movie Reviews
        mReviewsLoaderListener = new LoaderManager.LoaderCallbacks<List<MovieReview>>() {
            @Override
            public Loader<List<MovieReview>> onCreateLoader(final int movieId, Bundle bundle) {
                return new MovieReviewsLoader(MovieDetailActivity.this, mMovie.getMovieId(), MOVIE_REVIEW_LIMIT);
            }

            @Override
            public void onLoadFinished(Loader<List<MovieReview>> loader, List<MovieReview> movieReviews) {

                mMovieReviewAdapter.setMovieReviews(movieReviews);

                if(movieReviews.size() == 0) {
                    mReviewsLayout.setVisibility(View.GONE);
                }

                if(mReviewRVState != null) {
                    mMovieReviewRVLayoutManager.onRestoreInstanceState(mReviewRVState);
                    mReviewRVState = null;
                }
                if(scrollPositions != null) {
                    mScrollView.scrollTo(scrollPositions[0], scrollPositions[1]);
                    scrollPositions = null;
                }

            }

            @Override
            public void onLoaderReset(Loader<List<MovieReview>> loader) {

            }
        };

        boolean isConnected = NetworkUtils.checkForNetworkStatus(this);
        if(isConnected) {
            getSupportLoaderManager().initLoader(MOVIE_TRAILERS_LOADER_ID, null, this);
            getSupportLoaderManager().initLoader(MOVIE_REVIEWS_LOADER_ID, null, mReviewsLoaderListener);
        }

    }

    // Handles Loading Movie Trailer
    @Override
    public Loader<List<MovieTrailer>> onCreateLoader(int mMovieId, Bundle args) {
        return new MovieTrailersLoader(this, mMovie.getMovieId(), MOVIE_TRAILER_LIMIT);
    }

    @Override
    public void onLoadFinished(Loader<List<MovieTrailer>> loader, List<MovieTrailer> data) {

        mMovieTrailerAdapter.setMovieTrailers(data);

        if(data.size() == 0) {
            mTrailersLayout.setVisibility(View.GONE);
        }

        if(mTrailerRvState != null) {
            mMovieTrailerRVLayoutManager.onRestoreInstanceState(mTrailerRvState);
            mTrailerRvState = null;
        }
        if(scrollPositions != null) {
            mScrollView.scrollTo(scrollPositions[0], scrollPositions[1]);
            scrollPositions = null;
        }

    }

    @Override
    public void onLoaderReset(Loader<List<MovieTrailer>> loader) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(MOVIE_BUNDLE_KEY, mMovie);
        outState.putBoolean(MOVIE_STATE_KEY, mMovieState);

        mTrailerRvState = mMovieTrailerRVLayoutManager.onSaveInstanceState();
        outState.putParcelable(TRAILER_RV_STATE_KEY, mTrailerRvState);

        mReviewRVState = mMovieTrailerRVLayoutManager.onSaveInstanceState();
        outState.putParcelable(REVIEW_RV_STATE_KEY, mReviewRVState);

        scrollPositions = new int[] {mScrollView.getScrollX(), mScrollView.getScrollX()};
        outState.putIntArray(SCROLL_POSITION_KEY, scrollPositions);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMovie = savedInstanceState.getParcelable(MOVIE_BUNDLE_KEY);
        mMovieState = savedInstanceState.getBoolean(MOVIE_STATE_KEY);
        mTrailerRvState = savedInstanceState.getParcelable(TRAILER_RV_STATE_KEY);
        mReviewRVState = savedInstanceState.getParcelable(REVIEW_RV_STATE_KEY);
        scrollPositions = savedInstanceState.getIntArray(SCROLL_POSITION_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mReviewRVState != null) {
            mMovieTrailerRVLayoutManager.onRestoreInstanceState(mTrailerRvState);
        }
        if(mReviewRVState != null) {
            mMovieReviewRVLayoutManager.onRestoreInstanceState(mReviewRVState);
        }
    }

    private void populateMovieUI(){

        // Movie info
        setTitle(mMovie.getTitle());
        Picasso.with(this).load(mMovie.getBackdropPath()).into(mBackdrop);
        Picasso.with(this).load(mMovie.getPosterPath()).into(mPosterThumbnail);
        mMovieRating.setText(Double.toString(mMovie.getRating()) + "/10");
        mDateReleased.setText(mMovie.getReleaseDate().split("-")[0]);
        mSinopsys.setText(mMovie.getSynopsis());

        // Movie Trailer
        mMovieTrailerRVLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mMovieTrailerRecyclerView.setLayoutManager(mMovieTrailerRVLayoutManager);
        mMovieTrailerRecyclerView.setHasFixedSize(true);
        mMovieTrailerAdapter = new MovieTrailerAdapter(this);
        mMovieTrailerRecyclerView.setAdapter(mMovieTrailerAdapter);

        // Movie Review
        mMovieReviewRVLayoutManager = new LinearLayoutManager(this);
        mMovieReviewRecyclerView.setLayoutManager(mMovieReviewRVLayoutManager);
        mMovieReviewRecyclerView.setHasFixedSize(true);
        mMovieReviewAdapter = new MovieReviewAdapter();
        mMovieReviewRecyclerView.setAdapter(mMovieReviewAdapter);



    }

    private void initViewModel() {

        if (mMovie == null) return;

        MovieDetailViewModelFactory factory =
                new MovieDetailViewModelFactory(MovieDatabase.getInstance(MovieDetailActivity.this), mMovie.getMovieId());
        mViewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);
        mViewModel.getMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if(movie != null) {
                    mMovieState = true;
                    setColorFavoriteButton();
                }
            }
        });
    }

    @OnClick(R.id.fab_favorites)
    public void addOrRemoveFavorites() {

        AppExecutors task = AppExecutors.getInstance();

        if (mMovieState) {
            mMovieState = false;
            setColorFavoriteButton();
            task.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().delete(mMovie);
                }
            });
            Toast.makeText(MovieDetailActivity.this,
                    "Successfully removed from favorites", Toast.LENGTH_SHORT).show();
        }
        else {
            mMovieState = true;
            setColorFavoriteButton();

            task.diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insert(mMovie);
                }
            });
            Toast.makeText(MovieDetailActivity.this,
                    "Successfully added to favorites", Toast.LENGTH_SHORT).show();

        }

    }

    private void setColorFavoriteButton() {
        if (mMovieState) {
            int goldColor = ResourcesCompat.getColor(getResources(), R.color.goldStar, null);
            mAddFavoritesButton.setColorFilter(goldColor);
        }
        else {
            mAddFavoritesButton.setColorFilter(Color.WHITE);
        }
    }

    // This handles the movie Trailer onclick
    @Override
    public void onClick(MovieTrailer movieTrailer) {

        Uri location = Uri.parse(movieTrailer.getKey());
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, location);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(youtubeIntent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) startActivity(youtubeIntent);


    }

}