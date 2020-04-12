package com.jidesh.erosnowjidesh.ViewHolders

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies
import com.jidesh.erosnowjidesh.Models.Entities.Movie
import com.jidesh.erosnowjidesh.repository.Fav_repository

class PageViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: Fav_repository
    private val allFavMovies: LiveData<List<Fav_movies>>?
    private var mutableLiveData: MutableLiveData<List<Movie>>? = null


    init {
        repository = Fav_repository(application)
        allFavMovies = repository.allFavs
    }
    fun getSearch(s: String?): LiveData<List<Movie>> {
        mutableLiveData = MutableLiveData()
        mutableLiveData = repository.getNews(s!!)
        return mutableLiveData!!
    }

    fun inset(fav_movies: Fav_movies?) {
        repository.Insert(fav_movies)
    }

    fun delete(fav_movies: Fav_movies?) {
        repository.Delete(fav_movies)
    }

    fun search(search: String?) {
        repository.getNews(search!!)
    }

    fun check(a: Int): Int {
        return repository.find(a)
    }

}