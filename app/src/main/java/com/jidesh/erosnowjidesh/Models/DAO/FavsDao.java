package com.jidesh.erosnowjidesh.Models.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies;

import java.util.List;

@Dao
public interface  FavsDao {

    @Insert
     void insert(Fav_movies fav);

    @Delete
    void delete(Fav_movies Fav);

@Query("SELECT * FROM fav_table")
    LiveData<List<Fav_movies>>getAllFavs();

@Query("SELECT COUNT(*) FROM fav_table WHERE id IN (:favid)")
   Integer check(int favid);

}
