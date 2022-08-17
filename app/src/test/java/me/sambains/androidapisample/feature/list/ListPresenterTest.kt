package me.sambains.feature.list

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.core.models.TopRatedMovies
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import me.sambains.androidapisample.feature.list.ListContract
import me.sambains.androidapisample.feature.list.ListInteractor
import me.sambains.androidapisample.feature.list.ListPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.*

class ListPresenterTest {

    private lateinit var listView: ListContract.ListView
    private lateinit var listInteractor: ListContract.ListInteractor
    private lateinit var moviesExpectedResult: List<Movie>
    private lateinit var moviesExpectedError: Throwable
    private lateinit var listPresenter: ListContract.ListPresenter

    @Before
    fun setUp() {
        listView = mock(ListContract.ListView::class.java)

        val theMovieDbApi = mock(TheMovieDbApi::class.java)
        val schedulerProvider = mock(SchedulerProvider::class.java)
        val context = mock(Context::class.java)

        val gson = Gson()
        val topRatedMoviesExpectedResponse: TopRatedMovies = gson.fromJson(
            this::class.java.classLoader.getResource("movies.json").readText(),
            object : TypeToken<TopRatedMovies>() {}.type
        )
        moviesExpectedResult = topRatedMoviesExpectedResponse.movies
        moviesExpectedError = Throwable()

        `when`(schedulerProvider.backgroundThread())
            .thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.mainThread())
            .thenReturn(Schedulers.trampoline())
        `when`(theMovieDbApi.getTopRatedMovies(anyString()))
            .thenReturn(Single.just(topRatedMoviesExpectedResponse))
        `when`(context.getString(Mockito.anyInt()))
            .thenReturn("Oops! Something went wrong. Please try again.")

        listInteractor = ListInteractor(theMovieDbApi, schedulerProvider)
        listPresenter = ListPresenter(context, listView, listInteractor)
    }

    @Test
    fun testGetTopRatedMovies() {
        listPresenter.getMovies()
        verify(listView).showLoading()
    }

    @Test
    fun testMoviesAreShown() {
        listPresenter.onSuccess(moviesExpectedResult)
        verify(listView).hideLoading()
        verify(listView).showMovies(moviesExpectedResult)
    }

    @Test
    fun testMoviesErrorIsShown() {
        listPresenter.onError(moviesExpectedError)
        verify(listView).hideLoading()
        verify(listView).showError("Oops! Something went wrong. Please try again.")
    }
}