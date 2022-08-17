package me.sambains.androidapisample.core.dependencies

import dagger.Component
import me.sambains.androidapisample.core.base.BaseApplication
import me.sambains.androidapisample.feature.detail.DetailComponent
import me.sambains.androidapisample.feature.detail.DetailModule
import me.sambains.androidapisample.feature.list.ListAdapter
import me.sambains.androidapisample.feature.list.ListComponent
import me.sambains.androidapisample.feature.list.ListModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(baseApplication: BaseApplication)

    operator fun plus(listModule: ListModule): ListComponent

    fun inject(listAdapter: ListAdapter)

    operator fun plus(detailModule: DetailModule): DetailComponent
}