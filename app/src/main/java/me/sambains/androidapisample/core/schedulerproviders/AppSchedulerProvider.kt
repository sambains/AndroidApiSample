package me.sambains.androidapisample.core.schedulerproviders

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AppSchedulerProvider @Inject constructor() : SchedulerProvider {
    override fun mainThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun backgroundThread(): Scheduler {
        return Schedulers.io()
    }
}