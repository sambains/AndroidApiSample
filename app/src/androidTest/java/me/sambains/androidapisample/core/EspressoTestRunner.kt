package me.sambains.androidapisample.core

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import me.sambains.androidapisample.core.base.BaseApplicationTest

class EspressoTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, BaseApplicationTest::class.java.name, context)
    }
}