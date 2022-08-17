package me.sambains.androidapisample.feature.detail

import android.content.Context
import io.reactivex.disposables.CompositeDisposable
import me.sambains.androidapisample.core.base.BasePresenter
import me.sambains.androidapisample.core.models.Movie
import javax.inject.Inject

class DetailPresenter @Inject internal constructor(context: Context, detailView: DetailContract.DetailView,
private var detailInteractor: DetailContract.DetailInteractor?) : BasePresenter<DetailContract.DetailView>(context, detailView), DetailContract.DetailPresenter {
    private val compositeDisposable = CompositeDisposable()

    override fun detachView() {
        super.detachView()

        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }

        detailInteractor = null
    }

    override fun getMovie(movieId: Long) {
        getView().showLoading()

        detailInteractor?.let {
            compositeDisposable.add(it.getMovie(movieId)
                .subscribe { movie, throwable ->
                    if (throwable == null) {
                        onSuccess(movie)
                    } else {
                        onError(throwable)
                    }
                })
        }
    }

    override fun onSuccess(movie: Movie) {
        getView().hideLoading()
        getView().showMovie(movie)
    }

    override fun onError(e: Throwable) {
        getView().hideLoading()
        getView().showError(getErrorMessage(e))
    }

    override fun createDummyView(): DetailContract.DetailView {
        return object : DetailContract.DetailView {
            override fun getMovie(movieId: Long) {

            }

            override fun showLoading() {

            }

            override fun hideLoading() {

            }

            override fun showMovie(movie: Movie) {

            }

            override fun showError(errorMessage: String) {

            }
        }
    }
}