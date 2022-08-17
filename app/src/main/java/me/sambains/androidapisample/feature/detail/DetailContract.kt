package me.sambains.androidapisample.feature.detail

import io.reactivex.Single
import me.sambains.androidapisample.core.models.Movie

interface DetailContract {

    interface DetailView {
        fun getMovie(movieId: Long)

        fun showLoading()

        fun hideLoading()

        fun showMovie(movie: Movie)

        fun showError(errorMessage: String)
    }

    interface DetailPresenter {
        fun getMovie(movieId: Long)

        fun onSuccess(movie: Movie)

        fun onError(e: Throwable)
    }

    interface DetailInteractor {
        fun getMovie(movieId: Long): Single<Movie>
    }
}