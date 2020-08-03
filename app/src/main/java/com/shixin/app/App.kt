package com.shixin.app

import com.shixin.base.BaseApplication
import com.shixin.koin.module.AppModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@HiltAndroidApp
class App : BaseApplication() {
    override fun onCreate() {
        super.onCreate()

        // start Koin!
        startKoin {
            // declare used Android context
            androidContext(this@App)
            // declare modules
            modules(AppModule.appModule)
        }
    }
}