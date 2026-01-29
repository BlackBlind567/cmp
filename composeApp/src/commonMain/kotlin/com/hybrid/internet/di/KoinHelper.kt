package com.hybrid.internet.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration? = null) {
    startKoin {
        // This allows Android to pass its context
        appDeclaration?.invoke(this)
        // Load your shared modules
        modules(coreModule)
    }
}