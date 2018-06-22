package com.brunogtavares.popmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.brunogtavares.popmovies.model.Movie;

import java.util.List;

/**
 * Created by brunogtavares on 6/22/18.
 * Here we just need to save, retrieve and delete movies
 */

@Dao
public interface MovieDao {
    @Insert
    void insert(Movie movie);

    @Delete
    void delete (Movie... movies);

    @Query("SELECT * FROM movie")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM movie WHERE movieId = :movieId")
    List<Movie> findRepositoriesForMovie(final int movieId);
}
