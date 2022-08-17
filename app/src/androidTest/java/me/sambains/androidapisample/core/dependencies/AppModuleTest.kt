package me.sambains.androidapisample.core.dependencies

import android.content.Context
import dagger.Module
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.helpers.NetworkHelper
import org.mockito.Mockito.mock
import retrofit2.Retrofit

open class AppModuleTest(context: Context) : AppModule(context) {

    override fun providesApi(retrofit: Retrofit): TheMovieDbApi {
        return mock(TheMovieDbApi::class.java)
    }

    override fun providesNetworkHelper(): NetworkHelper {
        return mock(NetworkHelper::class.java)
    }
}