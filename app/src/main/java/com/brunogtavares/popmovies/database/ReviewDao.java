package com.brunogtavares.popmovies.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.brunogtavares.popmovies.model.MovieReview;

import java.util.List;

/**
 * Created by brunogtavares on 6/22/18.
 */

@Dao
public interface ReviewDao {
    @Insert
    void insertReview(MovieReview movieReview);

    @Delete
    void deletRview(MovieReview... movieReviews);

    @Query("SELECT * FROM MovieReview WHERE movieId = :movieId LIMIT :limit")
    List<MovieReview> loadAllReviewsForMovie(final int movieId, final int limit);
}
