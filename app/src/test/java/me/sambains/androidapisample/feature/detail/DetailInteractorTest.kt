package me.sambains.feature.detail

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import me.sambains.androidapisample.feature.detail.DetailContract
import me.sambains.androidapisample.feature.detail.DetailInteractor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock


class DetailInteractorTest {

    private lateinit var detailInteractor: DetailContract.DetailInteractor

    @Before
    fun setUp() {
        val theMovieDbApi = mock(TheMovieDbApi::class.java)
        val schedulerProvider = mock(SchedulerProvider::class.java)

        val gson = Gson()
        val movie: Movie = gson.fromJson(
            this::class.java.classLoader.getResource("movie.json").readText(),
            object : TypeToken<Movie>() {}.type
        )

        `when`(schedulerProvider.mainThread())
            .thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.backgroundThread())
            .thenReturn(Schedulers.trampoline())

        `when`(theMovieDbApi.getMovie(anyLong(), anyString()))
            .thenReturn(Single.just(movie))

        detailInteractor = DetailInteractor(theMovieDbApi, schedulerProvider)
    }

    @Test
    fun testGetMovieApi() {
        val movie = detailInteractor.getMovie(0).blockingGet()

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