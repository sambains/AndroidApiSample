package me.sambains.androidapisample.core.base

import android.content.Context
import me.sambains.androidapisample.R
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

abstract class BasePresenter<T>(protected val context: Context, private var view: T?) {
    private val dummyView: T

    init {
        dummyView = createDummyView()
    }

    abstract fun createDummyView(): T

    /**
     * Nullifies the view once it is not required so we can free up resources
     */
    open fun detachView() {
        view = null
    }

    /**
     * Attempts to get the view that has been created and is visible to the user.
     * If this cannot be retrieved, because it is null (therefore already detached),
     * we will use the dummyView which consists of empty implementation methods of the
     * view interface.
     */
    fun getView(): T {
        if (view == null) {
            Timber.tag(javaClass.canonicalName).d("view == null. Returning dummyView")
            return dummyView
        } else {
            @Suppress("UNCHECKED_CAST")
            return view as T
        }
    }

    /**
     * Attempts to match a API throwable to a string response for the user
     * @param throwable The throwable parameter that is passed from the API response
     * @return A string that expresses the value of the throwable
     */
    protected fun getErrorMessage(throwable: Throwable?): String {
        if (throwable?.message.isNullOrEmpty()) {
            return context.getString(R.string.error_message_default)
        }

        if (throwable is UnknownHostException) {
            return context.getString(R.string.error_message_unknown_host_exception)
        }

        return if (throwable is HttpException) {
            context.getString(R.string.error_message_network)
        } else context.getString(R.string.error_message_default)

    }
}