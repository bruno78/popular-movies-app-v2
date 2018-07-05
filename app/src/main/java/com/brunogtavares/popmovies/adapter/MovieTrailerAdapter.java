package com.brunogtavares.popmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brunogtavares.popmovies.R;
import com.brunogtavares.popmovies.model.MovieTrailer;
import com.brunogtavares.popmovies.webservice.ThemoviedbApiClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by brunogtavares on 6/26/18.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private final String LOG_TAG = MovieTrailerAdapter.class.getSimpleName();

    private List<MovieTrailer> mMovieTrailers;
    private Context mContext;

    private final MovieTrailerOnClickHandler mClickHandler;

    public MovieTrailerAdapter(MovieTrailerOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
        this.mMovieTrailers = new ArrayList<>();
    }

    public interface MovieTrailerOnClickHandler {
        void onClick(MovieTrailer movieTrailer);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_trailer_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mMovieTrailerLabel.setText("Trailer " + Integer.toString(position + 1));
        MovieTrailer trailer = mMovieTrailers.get(position);

        Picasso.with(mContext)
                .load(ThemoviedbApiClient.getTrailerThumbnail(trailer.getKey()))
                .into(holder.mMovieFrame);
    }

    @Override
    public int getItemCount() {
        return mMovieTrailers == null ? 0 : mMovieTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // @BindView(R.id.iv_movie_play) ImageView mMoviePlay;
        @BindView(R.id.iv_movie_frame) ImageView mMovieFrame;
        @BindView(R.id.tv_movie_trailer_label) TextView mMovieTrailerLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieTrailer movieTrailer = mMovieTrailers.get(adapterPosition);
            mClickHandler.onClick(movieTrailer);
        }
    }

    public void setMovieTrailers(List<MovieTrailer> movieTrailers) {
        this.mMovieTrailers = movieTrailers;
        notifyDataSetChanged();
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

}
