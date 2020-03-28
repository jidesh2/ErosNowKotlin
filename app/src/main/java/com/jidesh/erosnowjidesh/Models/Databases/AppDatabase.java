package com.jidesh.erosnowjidesh.Models.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jidesh.erosnowjidesh.Models.DAO.FavsDao;
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies;


@Database(entities = {Fav_movies.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;
    private static final String DB_NAME = "eros_fav";

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (appDatabase != null)
            return appDatabase;
        else {
            appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
            return appDatabase;
        }
    }

    public abstract FavsDao favsDao();
}
