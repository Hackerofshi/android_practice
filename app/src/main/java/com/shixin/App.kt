package com.shixin

import com.shixin.base.BaseApplication
import com.shixin.ui.jetpack.paging3.di.appModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidApp
class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        // start Koin!
        startKoin {
            // declare used Android context
            androidContext(this@App)
            // declare modules
            modules(appModule)
        }
    }

    companion object {
        lateinit var instance: App
            private set
    }
}