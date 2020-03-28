package com.jidesh.erosnowjidesh.retrofit;


import com.jidesh.erosnowjidesh.Models.Entities.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Service {

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int pageIndex);
    @GET("search/movie")
    Call<MoviesResponse> getSearchedMovies(@Query("api_key") String apiKey, @Query("query") String query);

}
