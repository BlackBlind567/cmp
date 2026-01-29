package com.hybrid.internet


import android.app.Application
import com.hybrid.internet.di.initKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@App)

        }
    }
}