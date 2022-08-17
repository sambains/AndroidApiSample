package me.sambains.androidapisample.core.base

import androidx.multidex.MultiDexApplication
import me.sambains.androidapisample.BuildConfig
import me.sambains.androidapisample.core.dependencies.AppComponent
import me.sambains.androidapisample.core.dependencies.AppModule
import me.sambains.androidapisample.core.dependencies.DaggerAppComponent
import timber.log.Timber

open class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        initApplication()

        appComponent = createAppComponent()
        appComponent.inject(this)
    }

    private fun initApplication() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    open fun createAppComponent(): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}