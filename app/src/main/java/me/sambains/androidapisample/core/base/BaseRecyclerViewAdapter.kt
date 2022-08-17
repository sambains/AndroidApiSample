package me.sambains.androidapisample.core.base

import androidx.recyclerview.widget.RecyclerView
import me.sambains.androidapisample.core.dependencies.AppComponent

abstract class BaseRecyclerViewAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    init {
        injectDependencies(BaseApplication.appComponent)
    }

    protected abstract fun injectDependencies(appComponent: AppComponent)
}