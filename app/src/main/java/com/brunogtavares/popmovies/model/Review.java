package com.brunogtavares.popmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by brunogtavares on 6/22/18.
 */

@Entity(foreignKeys = @ForeignKey(entity = Movie.class,
                                    parentColumns = "movieId",
                                    childColumns = "movieId",
                                    onDelete = CASCADE))
public class Review {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String author;
    private String content;
    private int movieId;

    public Review(int movieId, String author, String content) {
        this.movieId = movieId;
        this.author = author;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
