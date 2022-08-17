package me.sambains.androidapisample.core.helpers

class TimeHelper {

    fun getNumberOfHoursInMinutes(minutes: Int): Int {
        return minutes / 60
    }

    fun getNumberOfMinutesWithoutHours(minutes: Int): Int {
        return minutes % 60
    }
}