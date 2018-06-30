package com.brunogtavares.popmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brunogtavares on 5/10/18.
 * Using Parcelable for this model object so data can be passed across Activities and Fragments
 * Using Parcelable {@link: http://www.vogella.com/tutorials/AndroidParcelable/article.html}
 */

@Entity(tableName = "movie", indices = {@Index(value = {"movie_id"}, unique = true)})
public class Movie implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "movie_id")
    private int movieId;
    private String title;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;
    private String synopsis;
    private double rating;
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    public Movie( int movieId, String title, String posterPath, String backdropPath,
                  String synopsis, double rating, String releaseDate ) {
        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.synopsis = synopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public int getMovieId() { return movieId; }

    public void setMovieId(int id) { this.movieId = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() { return backdropPath; }

    public void setBackdropPath(String backDrop) { this.backdropPath = backDrop; }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "{" + "\n" +
                "MovieId: " + getMovieId() + "\n" +
                "title: " + getTitle() + "\n" +
                "poster path: " + getPosterPath() + "\n" +
                "backdrop path: " + getBackdropPath() + "\n" +
                "synopsis: " + getSynopsis() + "\n" +
                "average rating: " + getRating() + "\n" +
                "release date: " + getReleaseDate() + "\n" +
                "}";
    }


    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        movieId = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        synopsis = in.readString();
        releaseDate = in.readString();
        rating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(movieId);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(synopsis);
        parcel.writeString(releaseDate);
        parcel.writeDouble(rating);
    }
}
