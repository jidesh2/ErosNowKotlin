package com.jidesh.erosnowjidesh.ViewHolders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies
import com.jidesh.erosnowjidesh.repository.Fav_repository

class FavFragViewmodel(application: Application) : AndroidViewModel(application) {
    private val repository: Fav_repository
    val allFavMovies: LiveData<List<Fav_movies>>?
    fun inset(fav_movies: Fav_movies?) {
        repository.Insert(fav_movies)
    }

    fun delete(fav_movies: Fav_movies?) {
        repository.Delete(fav_movies)
    }

    fun check(a: Int): Int {
        return repository.find(a)
    }

    init {
        repository = Fav_repository(application)
        allFavMovies = repository.allFavs
    }
}