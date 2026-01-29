package com.hybrid.internet.app

import com.hybrid.internet.di.coreModule


import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(
            coreModule
        )
    }
}
