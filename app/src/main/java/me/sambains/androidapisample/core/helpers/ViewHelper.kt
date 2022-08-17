package me.sambains.androidapisample.core.helpers

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.SnackbarContentLayout
import me.sambains.androidapisample.R

class ViewHelper {

    /**
     * Creates and returns a SnackBar view that stylises the text, background colors
     * to match the Visit London colour guidelines
     */
    fun makeSnackbar(view: View, message: String, duration: Int, context: Context): Snackbar {
        val snackbar = Snackbar.make(view, message, duration)

        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
        val snackbarContentLayout = snackbarLayout.getChildAt(0) as SnackbarContentLayout

        val textView = snackbarContentLayout.getChildAt(0) as TextView
        textView.setTextColor(ContextCompat.getColor(context, R.color.white))

        snackbarLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white))

        return snackbar
    }

}