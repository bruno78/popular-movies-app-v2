package com.brunogtavares.popmovies;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brunogtavares.popmovies.database.AppDatabase;
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
    @BindView(R.id.fab_save_favorites) FloatingActionButton addFavorites;

    // Database
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        mDb = AppDatabase.getsInstance(getApplicationContext());


        populateUI();
    }

    private void populateUI(){
        Movie movie = getIntent().getParcelableExtra(MOVIE_BUNDLE_KEY);

        setTitle(movie.getTitle());
        Picasso.with(this).load(movie.getBackDropPath()).into(mBackdrop);
        Picasso.with(this).load(movie.getPosterPath()).into(mPosterThumbnail);
        mMovieRating.setText(Double.toString(movie.getRating()) + "/10");
        mDateReleased.setText(movie.getReleaseDate().split("-")[0]);
        mSinopsys.setText(movie.getSynopsis());
    }

    @OnClick(R.id.fab_save_favorites)
    public void addFavorites() {
        Toast.makeText(MovieDetailActivity.this, "It's working!", Toast.LENGTH_SHORT);
    }

}
