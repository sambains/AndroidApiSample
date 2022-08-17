package me.sambains.androidapisample.core.models

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id")
    var id: Long,

    @SerializedName("title")
    var title: String,

    @SerializedName("tagline")
    var tagline: String,

    @SerializedName("overview")
    var overview: String,

    @SerializedName("vote_average")
    var voteAverage: Double,

    @SerializedName("release_date")
    var releaseDate: String,

    @SerializedName("poster_path")
    var posterPath: String,

    @SerializedName("backdrop_path")
    var backdropPath: String,

    @SerializedName("runtime")
    var runtime: Int
)