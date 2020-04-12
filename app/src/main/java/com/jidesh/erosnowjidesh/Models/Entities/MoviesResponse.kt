package com.jidesh.erosnowjidesh.Models.Entities

import com.google.gson.annotations.SerializedName

class MoviesResponse {
    @SerializedName("page")
    var page = 0
    @SerializedName("results")
    lateinit var movies: List<Movie>
    fun getResults(): List<Movie> {
        return movies
    }
    @SerializedName("total_results")
    var totalResults = 0
    @SerializedName("total_pages")
    var totalPages = 0

}