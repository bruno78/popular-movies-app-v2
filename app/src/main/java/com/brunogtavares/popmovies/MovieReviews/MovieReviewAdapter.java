package com.brunogtavares.popmovies.MovieReviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brunogtavares.popmovies.R;
import com.brunogtavares.popmovies.model.MovieReview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brunogtavares on 6/26/18.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ViewHolder> {

    private List<MovieReview> mMovieReviews;

    public MovieReviewAdapter() {
        this.mMovieReviews = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_review_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieReview movieReview = mMovieReviews.get(position);
        holder.mMovieReviewContent.setText(movieReview.getContent());
        holder.mMovieReviewAuthor.setText("- by " + movieReview.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mMovieReviews == null ? 0 : mMovieReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_movie_review_content)
        TextView mMovieReviewContent;

        @BindView(R.id.tv_movie_review_author)
        TextView mMovieReviewAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setMovieReviews(List<MovieReview> movieReviews) {
        this.mMovieReviews = movieReviews;
        notifyDataSetChanged();
    }
}
