package com.business.cmpproject.app

import com.business.cmpproject.di.coreModule


import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            coreModule
        )
    }
}
