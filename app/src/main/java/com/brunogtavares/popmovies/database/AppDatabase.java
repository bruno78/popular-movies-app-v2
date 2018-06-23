package com.brunogtavares.popmovies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.brunogtavares.popmovies.model.Movie;
import com.brunogtavares.popmovies.model.MovieTrailer;
import com.brunogtavares.popmovies.model.Review;

/**
 * Created by brunogtavares on 6/22/18.
 */
@Database(entities = {Movie.class, MovieTrailer.class, Review.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "movie";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .allowMainThreadQueries().build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }
}
