package me.sambains.core.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.core.models.TopRatedMovies
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class TheMovieDbApiTest {

    private lateinit var theMovieDBApi: TheMovieDbApi

    @Before
    fun setUp() {
        theMovieDBApi = mock(TheMovieDbApi::class.java)
        val schedulerProvider = mock(SchedulerProvider::class.java)

        `when`(schedulerProvider.mainThread()).thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.backgroundThread()).thenReturn(Schedulers.trampoline())

        val gson = Gson()
        val topRatedMoviesExpectedResponse: TopRatedMovies = gson.fromJson(
            this::class.java.classLoader.getResource("movies.json").readText(),
            object : TypeToken<TopRatedMovies>() {}.type
        )
        val movie: Movie = gson.fromJson(
            this::class.java.classLoader.getResource("movie.json").readText(),
            object : TypeToken<Movie>() {}.type
        )

        `when`(theMovieDBApi.getTopRatedMovies(anyString()))
            .thenReturn(Single.just(topRatedMoviesExpectedResponse))
        `when`(theMovieDBApi.getMovie(anyLong(), anyString()))
            .thenReturn(Single.just(movie))
    }

    @Test
    fun testGetTopRatedMovies() {
        val topRatedMovies = theMovieDBApi.getTopRatedMovies("api_key").blockingGet()

        assertNotNull(topRatedMovies)

        val movies: List<Movie> = topRatedMovies.movies

        assertNotNull(movies)
        assertEquals(20, movies.size)

        val movie = movies[19]

        assertNotNull(movie)
        assertEquals(122, movie.id)
        assertEquals(8.5, movie.voteAverage, 0.0)
        assertEquals("The Lord of the Rings: The Return of the King", movie.title)
        assertEquals("/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg", movie.posterPath)
        assertEquals("/pm0RiwNpSja8gR0BTWpxo5a9Bbl.jpg", movie.backdropPath)
        assertEquals(
            "Aragorn is revealed as the heir to the ancient kings as he, Gandalf and the other members of the broken fellowship struggle to save Gondor from Sauron's forces. Meanwhile, Frodo and Sam take the ring closer to the heart of Mordor, the dark lord's realm.",
            movie.overview
        )
        assertEquals("2003-12-01", movie.releaseDate)
    }

    @Test
    fun testGetMovie() {
        val movie = theMovieDBApi.getMovie(anyLong(), anyString()).blockingGet()

        assertNotNull(movie)

        assertEquals(299536, movie.id)
        assertEquals("Avengers: Infinity War", movie.title)
        assertEquals("An entire universe. Once and for all.", movie.tagline)
        assertEquals(
            "As the Avengers and their allies have continued to protect the world from threats too large for any one hero to handle, a new danger has emerged from the cosmic shadows: Thanos. A despot of intergalactic infamy, his goal is to collect all six Infinity Stones, artifacts of unimaginable power, and use them to inflict his twisted will on all of reality. Everything the Avengers have fought for has led up to this moment - the fate of Earth and existence itself has never been more uncertain.",
            movie.overview
        )
        assertEquals(8.268, movie.voteAverage, 0.0)
        assertEquals("2018-04-25", movie.releaseDate)
        assertEquals(149, movie.runtime)
        assertEquals("/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg", movie.posterPath)
        assertEquals("/lmZFxXgJE3vgrciwuDib0N8CfQo.jpg", movie.backdropPath)
    }
}