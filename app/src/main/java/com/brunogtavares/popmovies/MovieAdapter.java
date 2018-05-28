package com.brunogtavares.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brunogtavares.popmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by brunogtavares on 5/19/18.
 * Read about parcelable: http://www.vogella.com/tutorials/AndroidParcelable/article.html
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMovieList;
    private Context mContext;
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }


    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {


        public ImageView mMovieImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(mMovieList.get(position));
        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        Picasso.with(mContext)
                .load(movie.getPosterPath())
                .into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if(mMovieList.isEmpty() || mMovieList == null) return 0;
        return mMovieList.size();
    }

    public void setMovieList(List<Movie> movieList) {
        this.mMovieList = movieList;
        notifyDataSetChanged();
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

}
