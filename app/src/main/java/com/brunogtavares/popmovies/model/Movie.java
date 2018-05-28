package com.brunogtavares.popmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by brunogtavares on 5/10/18.
 * Using Parcelable for this model object so data can be passed across Activities and Fragments
 * Using Parcelable {@link: http://www.vogella.com/tutorials/AndroidParcelable/article.html}
 */

public class Movie implements Parcelable {

    private int movieId;
    private String title;
    private String posterPath;
    private String backDropPath;
    private String synopsis;
    private double rating;
    private String releaseDate;

    public Movie( int id, String originalTitle, String posterImage, String backDrop,
                  String overview, double voteAverage, String releaseDate ) {
        this.movieId = id;
        this.title = originalTitle;
        this.posterPath = posterImage;
        this.backDropPath = backDrop;
        this.synopsis = overview;
        this.rating = voteAverage;
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

    public String getBackDropPath() { return backDropPath; }

    public void setBackDropPath(String backDrop) { this.backDropPath = backDrop; }

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
                "id: " + getMovieId() + "\n" +
                "title: " + getTitle() + "\n" +
                "poster path: " + getPosterPath() + "\n" +
                "backdrop path: " + getBackDropPath() + "\n" +
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
        backDropPath = in.readString();
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
        parcel.writeString(backDropPath);
        parcel.writeString(synopsis);
        parcel.writeString(releaseDate);
        parcel.writeDouble(rating);
    }
}
