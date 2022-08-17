package me.sambains.feature.detail

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import me.sambains.androidapisample.feature.detail.DetailContract
import me.sambains.androidapisample.feature.detail.DetailInteractor
import me.sambains.androidapisample.feature.detail.DetailPresenter
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*

class DetailPresenterTest {

    private lateinit var detailView: DetailContract.DetailView
    private lateinit var detailInteractor: DetailContract.DetailInteractor
    private lateinit var movieExpectedResult: Movie
    private lateinit var movieExpectedError: Throwable
    private lateinit var detailPresenter: DetailContract.DetailPresenter

    @Before
    fun setUp() {
        detailView = mock(DetailContract.DetailView::class.java)

        val theMovieDbApi = mock(TheMovieDbApi::class.java)
        val schedulerProvider = mock(SchedulerProvider::class.java)
        val context = mock(Context::class.java)

        val gson = Gson()
        movieExpectedResult = gson.fromJson(
            this::class.java.classLoader.getResource("movie.json").readText(),
            object : TypeToken<Movie>() {}.type
        )
        movieExpectedError = Throwable()

        `when`(schedulerProvider.backgroundThread())
            .thenReturn(Schedulers.trampoline())
        `when`(schedulerProvider.mainThread())
            .thenReturn(Schedulers.trampoline())
        `when`(theMovieDbApi.getMovie(anyLong(), anyString()))
            .thenReturn(Single.just(movieExpectedResult))
        `when`(context.getString(anyInt()))
            .thenReturn("Oops! Something went wrong. Please try again.")


        detailInteractor = DetailInteractor(theMovieDbApi, schedulerProvider)
        detailPresenter = DetailPresenter(context, detailView, detailInteractor)
    }

    @Test
    fun testGetMovie() {
        detailPresenter.getMovie(0)
        verify(detailView).showLoading()
    }

    @Test
    fun testMovieIsShown() {
        detailPresenter.onSuccess(movieExpectedResult)
        verify(detailView).hideLoading()
        verify(detailView).showMovie(movieExpectedResult)
    }

    @Test
    fun testMovieErrorIsShown() {
        detailPresenter.onError(movieExpectedError)
        verify(detailView).hideLoading()
        verify(detailView).showError("Oops! Something went wrong. Please try again.")
    }
}