package com.jidesh.erosnowjidesh.repository

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jidesh.erosnowjidesh.Models.DAO.FavsDao
import com.jidesh.erosnowjidesh.Models.Databases.AppDatabase
import com.jidesh.erosnowjidesh.Models.Entities.Fav_movies
import com.jidesh.erosnowjidesh.Models.Entities.Movie
import com.jidesh.erosnowjidesh.Models.Entities.MoviesResponse
import com.jidesh.erosnowjidesh.retrofit.RetrofitClient
import com.jidesh.erosnowjidesh.retrofit.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutionException

class Fav_repository(application: Application?) {

    var favsDao: FavsDao?
    private val movieService: Service
    private val movies: List<Movie>? = null
    var results: List<Movie>? = null
    var allFavs: LiveData<List<Fav_movies>>?
    fun Insert(fav: Fav_movies?) {
        insertAsync(favsDao).execute(fav)
    }
    init {
        val appdatabse = AppDatabase.getAppDatabase(application!!)
        favsDao = appdatabse!!.favsDao()
        allFavs = favsDao!!.allFavs
        movieService = RetrofitClient.getRetrofitInstance(application)!!.create(Service::class.java)
    }
    fun Delete(fav: Fav_movies?) {
        deleteAsync(favsDao).execute(fav)
    }

    fun find(id: Int): Int {
        var a = 0
        try {
            a = findSync(favsDao).execute(id).get()!!
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return a
    }

     class insertAsync(val favsDao: FavsDao?) : AsyncTask<Fav_movies, Void, Void>() {
       override fun doInBackground(vararg p0: Fav_movies): Void? {
            favsDao!!.insert(p0[0])
            return null
        }

    }

  class deleteAsync(val favsDao: FavsDao?) : AsyncTask<Fav_movies, Void, Void>() {
      override fun doInBackground(vararg p0: Fav_movies): Void? {
            favsDao!!.delete(p0[0])
            return null
        }

    }

  class findSync(val favsDao: FavsDao?) : AsyncTask<Int, Int, Int>() {
        override fun doInBackground(vararg p0: Int?): Int? {
            return favsDao!!.check(p0[0])
        }

    }

    fun getNews(search: String): MutableLiveData<List<Movie>> {
        val newsData = MutableLiveData<List<Movie>>()
        callSearchMoviesApi(search).enqueue(object : Callback<MoviesResponse?> {
            override fun onResponse(call: Call<MoviesResponse?>, response: Response<MoviesResponse?>) {
                if (response.isSuccessful) {
                    newsData.value = fetchResults(response)
                }
            }

            override fun onFailure(call: Call<MoviesResponse?>, t: Throwable) {}
        })
        return newsData
    }

    fun fetchResults(response: Response<MoviesResponse?>): List<Movie> {
        val topRatedMovies = response.body()
        return topRatedMovies!!.getResults()
    }

    private fun callSearchMoviesApi(search: String): Call<MoviesResponse> {
        return movieService.getSearchedMovies(
                "9b06479415c61ae2a3ae755c53f15f6b", search
        )
    }


}