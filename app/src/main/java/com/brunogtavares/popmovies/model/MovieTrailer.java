package com.brunogtavares.popmovies.model;

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
        parentColumns = "movieId",
        childColumns = "movieId",
        onDelete = CASCADE),
        indices = {@Index("movieId")})
public class MovieTrailer {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String key;
    private String movieId;

    public MovieTrailer(String key, String movieId) {
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

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
