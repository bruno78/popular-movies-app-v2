package com.brunogtavares.popmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.brunogtavares.popmovies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE_BUNDLE_KEY = "MOVIE_KEY";

    @BindView(R.id.iv_movie_backdrop) ImageView mBackdrop;
    @BindView(R.id.iv_movie_poster_thumbnail) ImageView mPosterThumbnail;
    @BindView(R.id.tv_rating) TextView mMovieRating;
    @BindView(R.id.tv_date_released) TextView mDateReleased;
    @BindView(R.id.tv_synopsis) TextView mSinopsys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);

        Movie movie = getIntent().getParcelableExtra(MOVIE_BUNDLE_KEY);

        setTitle(movie.getTitle());
        Picasso.with(this).load(movie.getBackDropPath()).into(mBackdrop);
        Picasso.with(this).load(movie.getPosterPath()).into(mPosterThumbnail);
        mMovieRating.setText(Double.toString(movie.getRating()) + "/10");
        mDateReleased.setText(movie.getReleaseDate().replaceAll("-", " "));
        mSinopsys.setText(movie.getSynopsis());

    }
}
