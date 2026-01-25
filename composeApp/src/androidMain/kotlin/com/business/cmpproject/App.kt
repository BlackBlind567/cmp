package com.business.cmpproject


import android.app.Application
import com.business.cmpproject.di.coreModule
import com.business.cmpproject.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@App)

        }
    }
}