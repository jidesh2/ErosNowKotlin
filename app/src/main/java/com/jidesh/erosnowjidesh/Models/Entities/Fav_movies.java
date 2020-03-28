package com.jidesh.erosnowjidesh.Models.Entities;

import android.widget.ImageView;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "fav_table")
public class Fav_movies {
    @PrimaryKey
    private int id;

    private String img;

    public Fav_movies(Integer id, String img) {
        this.id=id;
        this.img=img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
