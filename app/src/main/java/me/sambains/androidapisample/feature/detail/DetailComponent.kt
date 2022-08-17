package me.sambains.androidapisample.feature.detail

import dagger.Subcomponent

@Subcomponent(modules = [DetailModule::class])
interface DetailComponent {

    fun inject(detailActivity: DetailActivity)
}