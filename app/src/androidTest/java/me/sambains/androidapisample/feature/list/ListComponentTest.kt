package me.sambains.androidapisample.feature.list

import dagger.Subcomponent

@Subcomponent(modules = [ListModule::class])
interface ListComponentTest : ListComponent {

    fun inject(listActivityTest: ListActivityTest)
}