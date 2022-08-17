package me.sambains.androidapisample.core.schedulerproviders

import io.reactivex.Scheduler

interface SchedulerProvider {

    fun mainThread(): Scheduler

    fun backgroundThread(): Scheduler
}