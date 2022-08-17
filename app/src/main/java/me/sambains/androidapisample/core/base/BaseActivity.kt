package me.sambains.androidapisample.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.sambains.androidapisample.core.dependencies.AppComponent

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injectDependencies(BaseApplication.appComponent)
    }

    protected abstract fun injectDependencies(appComponent: AppComponent)
}