package me.sambains.androidapisample.core.api

import io.reactivex.Single
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.core.models.TopRatedMovies
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDbApi {

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String
    ): Single<TopRatedMovies>

    @GET("movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: Long,
        @Query("api_key") apiKey: String
    ): Single<Movie>
}