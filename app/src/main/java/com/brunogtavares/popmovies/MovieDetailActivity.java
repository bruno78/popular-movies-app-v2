package com.brunogtavares.popmovies;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brunogtavares.popmovies.database.MovieDatabase;
import com.brunogtavares.popmovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_BUNDLE_KEY = "MOVIE_KEY";

    @BindView(R.id.iv_movie_backdrop) ImageView mBackdrop;
    @BindView(R.id.iv_movie_poster_thumbnail) ImageView mPosterThumbnail;
    @BindView(R.id.tv_rating) TextView mMovieRating;
    @BindView(R.id.tv_date_released) TextView mDateReleased;
    @BindView(R.id.tv_synopsis) TextView mSinopsys;
    @BindView(R.id.fab_save_favorites) FloatingActionButton mAddFavoritesButton;

    private Movie mMovie;
    private Movie mMovieDb;

    // Database
    private MovieDatabase mDb;

    private AppExecutors mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
        mTask = AppExecutors.getInstance();
        mDb = MovieDatabase.getsInstance(getApplicationContext());
        mMovie = getIntent().getParcelableExtra(MOVIE_BUNDLE_KEY);
        mTask.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mMovieDb = mDb.movieDao().getMovieById(mMovie.getMovieId()).getValue();
            }
        });

        if (mMovieDb != null) {
            mMovie = mMovieDb;
        }
        populateUI(mMovie);
    }

    private void populateUI(Movie movie){

        // TODO Needs to get movie id and pass as parameter to get the review
        // and trailers

        setTitle(movie.getTitle());
        Picasso.with(this).load(movie.getBackDropPath()).into(mBackdrop);
        Picasso.with(this).load(movie.getPosterPath()).into(mPosterThumbnail);
        mMovieRating.setText(Double.toString(mMovie.getRating()) + "/10");
        mDateReleased.setText(movie.getReleaseDate().split("-")[0]);
        mSinopsys.setText(movie.getSynopsis());

        setColorFavoriteButton();


    }

    @OnClick(R.id.fab_save_favorites)
    public void addFavorites() {

        if (mMovie.isFavorite()) {

            mMovie.setFavorite(false);
            setColorFavoriteButton();

            mTask.diskIO().execute(new Runnable() {
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

            mTask.diskIO().execute(new Runnable() {
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

}
