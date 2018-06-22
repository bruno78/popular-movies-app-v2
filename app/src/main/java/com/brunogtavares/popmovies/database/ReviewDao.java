package com.brunogtavares.popmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.brunogtavares.popmovies.model.Review;

import java.util.List;

/**
 * Created by brunogtavares on 6/22/18.
 */

@Dao
public interface ReviewDao {
    @Insert
    void insertReview(Review review);

    @Delete
    void deletRview(Review... reviews);

    @Query("SELECT * FROM review WHERE movieId=:movieId")
    List<Review> loadAllReviewsForMovie(final int movieId);
}
