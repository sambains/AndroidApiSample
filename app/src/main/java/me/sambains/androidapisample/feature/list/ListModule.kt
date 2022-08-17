package me.sambains.androidapisample.feature.list

import android.content.Context
import dagger.Module
import dagger.Provides
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider

@Module
class ListModule internal constructor(private val listView: ListContract.ListView) {

    @Provides
    internal fun providesListView(): ListContract.ListView {
        return listView
    }

    @Provides
    internal fun providesListPresenter(context: Context, listView: ListContract.ListView, listInteractor: ListContract.ListInteractor): ListContract.ListPresenter {
        return ListPresenter(context, listView, listInteractor)
    }

    @Provides
    internal fun providesListInteractor(theMovieDbApi: TheMovieDbApi, schedulerProvider: SchedulerProvider): ListContract.ListInteractor {
        return ListInteractor(theMovieDbApi, schedulerProvider)
    }

    @Provides
    internal fun providesListAdapter(context: Context): ListAdapter {
        return ListAdapter(context)
    }
}