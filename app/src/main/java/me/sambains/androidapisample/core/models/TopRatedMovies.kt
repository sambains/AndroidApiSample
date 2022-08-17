package me.sambains.androidapisample.core.models

import com.google.gson.annotations.SerializedName

data class TopRatedMovies (
    @SerializedName("results")
    var movies: List<Movie>
)