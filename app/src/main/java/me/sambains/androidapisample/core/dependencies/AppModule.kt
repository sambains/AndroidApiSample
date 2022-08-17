package me.sambains.androidapisample.core.dependencies

import android.content.Context
import dagger.Module
import dagger.Provides
import me.sambains.androidapisample.BuildConfig
import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.helpers.NetworkHelper
import me.sambains.androidapisample.core.helpers.TimeHelper
import me.sambains.androidapisample.core.helpers.ViewHelper
import me.sambains.androidapisample.core.schedulerproviders.AppSchedulerProvider
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class AppModule(context: Context) {

    private val applicationContext: Context = context.applicationContext

    @Provides
    @Singleton
    internal fun providesApplicationContext(): Context {
        return applicationContext
    }

    @Provides
    @Singleton
    internal fun providesSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }

    @Provides
    @Singleton
    internal open fun providesApi(retrofit: Retrofit): TheMovieDbApi {
        return retrofit.create(TheMovieDbApi::class.java)
    }

    @Provides
    @Singleton
    internal fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    internal fun providesOkHttpClient(
        cache: Cache,
        networkHelper: NetworkHelper,
        context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor { chain ->
                val builder = chain.request().newBuilder()

                /*
                         How the cache works
                         ===================

                         Scenario: Network call response has already been cached.

                         If the max-age has not yet been passed, then any network calls will be intercepted by the cache
                         If the max-age has been passed, then:
                                         If there is a network connection, the new response will be cached
                                         If there is no network connection, we will add custom cache-control header to
                                            allow the response to be stale for (see value of max-stale in REQUEST_HEADER_CACHE_CONTROL_VALUE_OFFLINE)
                     */

                if (!networkHelper.hasNetworkConnection(context)) {
                    builder.addHeader(
                        REQUEST_HEADER_CACHE_CONTROL_NAME,
                        REQUEST_HEADER_CACHE_CONTROL_VALUE_OFFLINE
                    )
                }

                chain.proceed(builder.build())
            }
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())

                // Only cache the response if the response code is 200.
                if (200 != response.code()) {
                    response.newBuilder()
                        .header(
                            REQUEST_HEADER_CACHE_CONTROL_NAME,
                            REQUEST_HEADER_CACHE_CONTROL_VALUE_NONE
                        )
                        .build()
                } else response
            }
            .addInterceptor(
                HttpLoggingInterceptor { message ->
                    Timber.tag(TAG).d(message)
                }
                    .setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
            )
            .build()
    }

    @Provides
    @Singleton
    internal open fun providesNetworkHelper(): NetworkHelper {
        return NetworkHelper()
    }

    @Provides
    @Singleton
    internal fun providesTimeHelper(): TimeHelper {
        return TimeHelper()
    }

    @Provides
    @Singleton
    internal fun providesViewHelper(): ViewHelper {
        return ViewHelper()
    }

    @Provides
    @Singleton
    internal fun providesCache(context: Context): Cache {
        return Cache(File(context.cacheDir.toString() + API_CACHE_DIRECTORY), (1024 * 1024 * 5).toLong()) // 5 MB
    }

    companion object {
        private val TAG = AppModule::class.java.canonicalName!!

        private const val API_CACHE_DIRECTORY = "/api_cache"
        const val REQUEST_HEADER_CACHE_CONTROL_NAME = "Cache-Control"
        private const val REQUEST_HEADER_CACHE_CONTROL_VALUE_OFFLINE =
            "public, max-stale=31540000" // 1 year
        const val REQUEST_HEADER_CACHE_CONTROL_VALUE_NONE = "no-cache, no-store"
    }
}