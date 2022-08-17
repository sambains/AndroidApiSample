package me.sambains.androidapisample.core.base

import me.sambains.androidapisample.core.dependencies.AppComponentTest
import me.sambains.androidapisample.core.dependencies.AppModuleTest
import me.sambains.androidapisample.core.dependencies.DaggerAppComponentTest

open class BaseApplicationTest : BaseApplication() {

    override fun createAppComponent(): AppComponentTest {
        return DaggerAppComponentTest.builder()
            .appModule(AppModuleTest(this))
            .build()
    }

    companion object {
        fun getAppComponent(): AppComponentTest {
            return appComponent as AppComponentTest
        }
    }
}