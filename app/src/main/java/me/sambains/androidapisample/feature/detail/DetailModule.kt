package me.sambains.androidapisample.feature.detail

import android.content.Context
import dagger.Module
import dagger.Provides
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider

@Module
open class DetailModule internal constructor(private val detailView: DetailContract.DetailView) {

    @Provides
    internal fun providesDetailView(): DetailContract.DetailView {
        return detailView
    }

    @Provides
    internal fun providesDetailPresenter(context: Context, detailView: DetailContract.DetailView, detailInteractor: DetailContract.DetailInteractor): DetailContract.DetailPresenter {
        return DetailPresenter(context, detailView, detailInteractor)
    }

    @Provides
    internal fun providesDetailInteractor(theMovieDbApi: TheMovieDbApi, schedulerProvider: SchedulerProvider): DetailContract.DetailInteractor {
        return DetailInteractor(theMovieDbApi, schedulerProvider)
    }
}