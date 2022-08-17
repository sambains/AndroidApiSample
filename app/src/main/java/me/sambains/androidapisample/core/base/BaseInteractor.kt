package me.sambains.androidapisample.core.base

import me.sambains.androidapisample.core.api.TheMovieDbApi
import me.sambains.androidapisample.core.schedulerproviders.SchedulerProvider

open class BaseInteractor(protected val theMovieDbApi: TheMovieDbApi, protected val schedulerProvider: SchedulerProvider,
)