package com.brunogtavares.popmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.brunogtavares.popmovies.model.Movie;

import java.util.List;

/**
 * Created by brunogtavares on 6/22/18.
 * Here we just need to save, retrieve and delete movies
 */

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Delete
    void delete (Movie... movie);

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movie WHERE movie_id = :id")
    LiveData<Movie> getMovieById(int id);

}
