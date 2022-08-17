package me.sambains.androidapisample.feature.detail

import dagger.Subcomponent

@Subcomponent(modules = [DetailModule::class])
interface DetailComponentTest : DetailComponent {

    fun inject(detailActivityTest : DetailActivityTest)
}