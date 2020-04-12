package com.jidesh.erosnowjidesh.Models.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies

@Dao
interface FavsDao {
    @Insert
    fun insert(fav: Fav_movies)

    @Delete
    fun delete(Fav: Fav_movies)

    @get:Query("SELECT * FROM fav_table")
    val allFavs: LiveData<List<Fav_movies>>?

    @Query("SELECT COUNT(*) FROM fav_table WHERE id IN (:favid)")
    fun check(favid: Int?): Int?
}