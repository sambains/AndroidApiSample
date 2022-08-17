package me.sambains.androidapisample.core.base

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import me.sambains.androidapisample.R

abstract class BaseToolbarActivity : BaseActivity() {

    protected abstract fun getToolbar(): Toolbar

    protected abstract val toolbarNavigationIcon: Int

    protected abstract val toolbarTitle: String?

    protected abstract fun onNavigationClickListenerAction()

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        setupToolbar()
    }

    /**
     * Sets up the toolbar using the values set by child Activities through the
     * abstract methods in this class. This allows the toolbar to be dynamic
     * across all activities.
     */
    private fun setupToolbar() {
        if (toolbarNavigationIcon != 0) {
            getToolbar().setNavigationIcon(toolbarNavigationIcon)
            getToolbar().setNavigationContentDescription(R.string.toolbar_navigation_icon)
            getToolbar().setNavigationOnClickListener { onNavigationClickListenerAction() }
        }
        getToolbar().title = toolbarTitle
    }
}