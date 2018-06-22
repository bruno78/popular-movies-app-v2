package com.brunogtavares.popmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.brunogtavares.popmovies.model.MovieTrailer;

/**
 * Created by brunogtavares on 6/22/18.
 */
@Dao
public interface MovieTrailerDao {

    @Insert
    void insertMovieTrailer(MovieTrailer movieTrailer);

    @Delete
    void deleteMovieTrailer(MovieTrailer... movieTrailers);

    @Query("SELECT * FROM movietrailer WHERE movieId=:movieId")
    void loadAllMovieTrailersForMovie(final int movieId);
}
