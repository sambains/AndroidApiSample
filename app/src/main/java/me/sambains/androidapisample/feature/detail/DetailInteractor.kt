package me.sambains.androidapisample.feature.detail

import io.reactivex.Single
import me.sambains.androidapisample.BuildConfig
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.base.BaseInteractor
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import javax.inject.Inject

class DetailInteractor @Inject internal constructor(
    theMovieDbApi: TheMovieDbApi, schedulerProvider: SchedulerProvider
) : BaseInteractor(theMovieDbApi, schedulerProvider), DetailContract.DetailInteractor {

    override fun getMovie(movieId: Long): Single<Movie> {
        return theMovieDbApi.getMovie(movieId, BuildConfig.API_KEY)
            .subscribeOn(schedulerProvider.backgroundThread())
            .observeOn(schedulerProvider.mainThread())
    }
}