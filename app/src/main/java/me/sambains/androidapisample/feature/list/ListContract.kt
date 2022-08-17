package me.sambains.androidapisample.feature.list

import io.reactivex.Single
import me.sambains.androidapisample.core.models.Movie

interface ListContract {

    interface ListView {
        fun getMovies()

        fun showLoading()

        fun hideLoading()

        fun showMovies(movies: List<Movie>)

        fun showError(errorMessage: String)

        fun openMovieDetail(movieId: Long)
    }

    interface ListPresenter {
        fun getMovies()

        fun onSuccess(movies: List<Movie>)

        fun onError(e: Throwable)
    }

    interface ListInteractor {
        fun getMovies(): Single<List<Movie>>
    }
}