package com.brunogtavares.popmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.brunogtavares.popmovies.model.MovieTrailer;

import java.util.List;

/**
 * Created by brunogtavares on 6/26/18.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.ViewHolder> {

    private List<MovieTrailer> mMovieTrailers;
    private Context mContext;
    private final MovieTrailerOnClickHandler mClickHandler;

    public interface MovieTrailerONClickHandler {
        void o
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
