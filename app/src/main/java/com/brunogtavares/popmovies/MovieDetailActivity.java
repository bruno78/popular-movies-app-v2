package com.brunogtavares.popmovies;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.brunogtavares.popmovies.adapter.MovieTrailerAdapter;
import com.brunogtavares.popmovies.database.MovieDatabase;
import com.brunogtavares.popmovies.model.Movie;
import com.brunogtavares.popmovies.model.MovieTrailer;
import com.brunogtavares.popmovies.webservice.NetworkUtils;
import com.brunogtavares.popmovies.webservice.ThemoviedbApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieTrailer>>, MovieTrailerAdapter.MovieTrailerOnClickHandler {

    private static final String MOVIE_BUNDLE_KEY = "MOVIE_KEY";
    private static final int MOVIE_TRAILERS_LOADER_ID = 101;

    // This number will be adjustable for shared preferences later
    private static final int MOVIE_TRAILER_LIMIT = 3;
    private static final int MOVIE_REVIEW_LIMIT = 3;

    private RecyclerView.LayoutManager mMovieTrailerRVLayoutManager;
    private MovieTrailerAdapter mMovieTrailerAdapter;

    private static Parcelable mTrailerRvState;
    private static int[] scrollPositions;

    @BindView(R.id.iv_movie_backdrop) ImageView mBackdrop;
    @BindView(R.id.iv_movie_poster_thumbnail) ImageView mPosterThumbnail;
    @BindView(R.id.tv_rating) TextView mMovieRating;
    @BindView(R.id.tv_date_released) TextView mDateReleased;
    @BindView(R.id.tv_synopsis) TextView mSinopsys;
    @BindView(R.id.fab_favorites) FloatingActionButton mAddFavoritesButton;
    @BindView(R.id.rv_movie_trailer) RecyclerView mMovieTrailerRecyclerView;
    @BindView(R.id.sv_movie_detail) ScrollView mScrollView;

    private Movie mMovie;

    // Database
    private MovieDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        mDb = MovieDatabase.getInstance(getApplicationContext());

        populateUI();

        mMovieTrailerRVLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mMovieTrailerRecyclerView.setLayoutManager(mMovieTrailerRVLayoutManager);
        mMovieTrailerRecyclerView.setHasFixedSize(true);
        mMovieTrailerAdapter = new MovieTrailerAdapter(this);

        boolean isConnected = NetworkUtils.checkForNetworkStatus(this);
        if(isConnected) {
            getSupportLoaderManager().initLoader(MOVIE_TRAILERS_LOADER_ID, null, this);
        }


    }

    private void populateUI(){
        mMovie = getIntent().getParcelableExtra(MOVIE_BUNDLE_KEY);
        // Needs to get movie id and pass as parameter to get the review
        // and trailers

        setTitle(mMovie.getTitle());
        Picasso.with(this).load(mMovie.getBackDropPath()).into(mBackdrop);
        Picasso.with(this).load(mMovie.getPosterPath()).into(mPosterThumbnail);
        mMovieRating.setText(Double.toString(mMovie.getRating()) + "/10");
        mDateReleased.setText(mMovie.getReleaseDate().split("-")[0]);
        mSinopsys.setText(mMovie.getSynopsis());

        setColorFavoriteButton();


    }

    @OnClick(R.id.fab_favorites)
    public void addFavorites() {

        AppExecutors task = AppExecutors.getInstance();

        if (mMovie.isFavorite()) {

            mMovie.setFavorite(false);
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

            mMovie.setFavorite(true);
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
        if (mMovie.isFavorite()) {
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

    // Handles Movie Trailer TASK
    @Override
    public Loader<List<MovieTrailer>> onCreateLoader(int mMovieId, Bundle args) {
        return new AsyncTaskLoader<List<MovieTrailer>>(MovieDetailActivity.this) {

            List<MovieTrailer> movieTrailers = new ArrayList<>();

            @Override
            public List<MovieTrailer> loadInBackground() {

                movieTrailers =
                        ThemoviedbApiClient.getMovietrailers(mMovie.getMovieId(), MOVIE_TRAILER_LIMIT);
                return movieTrailers;
            }

            @Override
            protected void onStartLoading() {
                if (movieTrailers.size() > 0) {
                    deliverResult(movieTrailers);
                } else {
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable List<MovieTrailer> data) {
                movieTrailers = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<MovieTrailer>> loader, List<MovieTrailer> data) {

        mMovieTrailerAdapter.setMovieTrailers(data);

    }

    @Override
    public void onLoaderReset(Loader<List<MovieTrailer>> loader) {

    }
}