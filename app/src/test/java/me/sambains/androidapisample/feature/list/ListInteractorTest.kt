package me.sambains.feature.list

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.models.TopRatedMovies
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import me.sambains.androidapisample.feature.list.ListContract
import me.sambains.androidapisample.feature.list.ListInteractor
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock

class ListInteractorTest {

    private lateinit var listInteractor: ListContract.ListInteractor

    @Before
    fun setUp() {
        val theMovieDbApi = mock(TheMovieDbApi::class.java)
        val schedulerProvider = mock(SchedulerProvider::class.java)

        val gson = Gson()
        val topRatedMoviesExpectedResponse: TopRatedMovies = gson.fromJson(
            this::class.java.classLoader.getResource("movies.json").readText(),
            object : TypeToken<TopRatedMovies>() {}.type
        )

        Mockito.`when`(schedulerProvider.mainThread())
            .thenReturn(Schedulers.trampoline())
        Mockito.`when`(schedulerProvider.backgroundThread())
            .thenReturn(Schedulers.trampoline())

        Mockito.`when`(theMovieDbApi.getTopRatedMovies(anyString()))
            .thenReturn(Single.just(topRatedMoviesExpectedResponse))

        listInteractor = ListInteractor(theMovieDbApi, schedulerProvider)
    }

    @Test
    fun testGetTopRatedMovies() {
        val movies = listInteractor.getMovies().blockingGet()

        assertNotNull(movies)
        assertEquals(20, movies.size);

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
}