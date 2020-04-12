package com.jidesh.erosnowjidesh.Models.Databases

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jidesh.erosnowjidesh.Models.DAO.FavsDao
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies

@Database(entities = [Fav_movies::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favsDao(): FavsDao?

    companion object {
        var appDatabase: AppDatabase? = null
        const val DB_NAME = "eros_fav"

        @Synchronized
        fun getAppDatabase(context: Context): AppDatabase? {
            return if (appDatabase != null) appDatabase else {
                appDatabase = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                return appDatabase
            }
        }
        fun destroyDataBase(){
            appDatabase = null
        }
    }
}