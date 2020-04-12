package com.jidesh.erosnowjidesh.Models.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_table")
data class Fav_movies(@field:PrimaryKey val id: Int, val img: String)