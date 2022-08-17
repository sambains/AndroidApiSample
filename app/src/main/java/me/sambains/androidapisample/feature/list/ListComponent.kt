package me.sambains.androidapisample.feature.list

import dagger.Subcomponent

@Subcomponent(modules = [ListModule::class])
interface ListComponent {

    fun inject(listActivity: ListActivity)
}