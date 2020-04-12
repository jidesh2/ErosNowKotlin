package com.jidesh.erosnowjidesh.retrofit

import com.jidesh.erosnowjidesh.Models.Entities.Movie
import com.jidesh.erosnowjidesh.Models.Entities.MoviesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface Service {
    @GET("movie/top_rated")
    fun getPopularMovies(@Query("api_key") apiKey: String?, @Query("page") pageIndex: Int): Call<MoviesResponse>

    @GET("search/movie")
    fun getSearchedMovies(@Query("api_key") apiKey: String?, @Query("query") query: String?): Call<MoviesResponse>
    @GET("movie/{movie_id}")
    fun getMovieById(@Path("movie_id") movie_id:String?,@Query("api_key")apiKey: String?):Call<Movie>
}