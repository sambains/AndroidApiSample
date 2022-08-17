package me.sambains.androidapisample.core.dependencies

import dagger.Component
import me.sambains.androidapisample.core.api.TheMovieDbApiCacheTest
import me.sambains.androidapisample.feature.detail.DetailComponentTest
import me.sambains.androidapisample.feature.detail.DetailModule
import me.sambains.androidapisample.feature.list.ListComponentTest
import me.sambains.androidapisample.feature.list.ListModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponentTest : AppComponent {

    override operator fun plus(listModule: ListModule): ListComponentTest

    override operator fun plus(detailModule: DetailModule): DetailComponentTest

    fun inject(theMovieDbApiCacheTest: TheMovieDbApiCacheTest)
}