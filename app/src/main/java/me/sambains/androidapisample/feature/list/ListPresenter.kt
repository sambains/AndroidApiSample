package me.sambains.androidapisample.feature.list

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import me.sambains.androidapisample.core.base.BasePresenter
import me.sambains.androidapisample.core.models.Movie
import javax.inject.Inject

class ListPresenter @Inject internal constructor(
    context: Context,
    listView: ListContract.ListView,
    private var listInteractor: ListContract.ListInteractor?
) : BasePresenter<ListContract.ListView>(
    context, listView
), ListContract.ListPresenter {
    private val compositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }

        listInteractor = null
    }

    override fun getMovies() {
        getView().showLoading()

        listInteractor?.let {
            compositeDisposable.add(it.getMovies()
                .subscribe { movies, throwable ->
                    if (throwable == null) {
                        onSuccess(movies)
                    } else {
                        onError(throwable)
                    }
                })
        }
    }

    override fun onSuccess(movies: List<Movie>) {
        getView().hideLoading()
        getView().showMovies(movies)
    }

    override fun onError(e: Throwable) {
        getView().hideLoading()
        getView().showError(getErrorMessage(e))
    }

    override fun createDummyView(): ListContract.ListView {
        return object : ListContract.ListView {
            override fun getMovies() {

            }

            override fun showLoading() {

            }

            override fun hideLoading() {

            }

            override fun showMovies(movies: List<Movie>) {

            }

            override fun showError(errorMessage: String) {

            }

            override fun openMovieDetail(movieId: Long) {

            }
        }
    }
}