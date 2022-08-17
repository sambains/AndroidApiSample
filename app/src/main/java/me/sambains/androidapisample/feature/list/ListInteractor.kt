package me.sambains.androidapisample.feature.list

import io.reactivex.Single
import me.sambains.androidapisample.BuildConfig
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.base.BaseInteractor
import me.sambains.androidapisample.core.models.Movie
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import javax.inject.Inject

class ListInteractor @Inject internal constructor(
    theMovieDbApi: TheMovieDbApi,
    schedulerProvider: SchedulerProvider
) : BaseInteractor(theMovieDbApi, schedulerProvider), ListContract.ListInteractor {

    override fun getMovies(): Single<List<Movie>> {
        return theMovieDbApi.getTopRatedMovies(BuildConfig.API_KEY)
            .map { topRatedMovies ->
                topRatedMovies.movies
            }
            .subscribeOn(schedulerProvider.backgroundThread())
            .observeOn(schedulerProvider.mainThread())
    }
}