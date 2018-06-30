package com.brunogtavares.popmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by brunogtavares on 6/22/18.
 * id (movie Id)
 * results
 *      author
 *      content
 */

@Entity(foreignKeys = @ForeignKey(entity = Movie.class,
        parentColumns = "movie_id",
        childColumns = "movie_id",
        onDelete = CASCADE),
        indices = {@Index("movie_id")})
public class MovieTrailer {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String key;
    @ColumnInfo(name = "movie_id")
    private int movieId;

    public MovieTrailer(String key, int movieId) {
        this.key = key;
        this.movieId = movieId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
