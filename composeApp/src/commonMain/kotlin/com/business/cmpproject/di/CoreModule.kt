package com.business.cmpproject.di

import com.business.cmpproject.core.dispatchers.AppDispatchers
import com.business.cmpproject.core.network.ApiClient
import com.business.cmpproject.core.network.TokenProvider
import com.business.cmpproject.core.session.SessionManager
import com.business.cmpproject.core.storage.LocalStorage
import com.business.cmpproject.data.remote.AuthApi
import com.business.cmpproject.data.remote.DashboardApi
import com.business.cmpproject.domain.repository.AuthRepository
import com.business.cmpproject.domain.repository.AuthRepositoryImpl
import com.business.cmpproject.domain.repository.dashboard.DashboardRepository
import com.business.cmpproject.domain.repository.dashboard.DashboardRepositoryImpl
import com.business.cmpproject.presentation.features.home.HomeScreenModel
import com.business.cmpproject.presentation.features.login.LoginScreenModel
import com.business.cmpproject.presentation.features.otp.OtpScreenModel
import com.business.cmpproject.presentation.features.splash.SplashScreenModel
import org.koin.dsl.module

val coreModule = module {

    // ---- Network ----
    single { TokenProvider(get()) }

    single { ApiClient(get()) }                 // ApiClient instance
    single { get<ApiClient>().client }           // HttpClient exposed

    // ---- Dispatchers ----
    single { AppDispatchers() }

    // ---- Storage ----
    single { LocalStorage() }

    // ---- Session ----
    single { SessionManager(get()) }

    // ---- API ----
    single { AuthApi(get()) }                    // HttpClient injected
    single { DashboardApi(get()) }                    // HttpClient injected

    // ---- Repository ----
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<DashboardRepository> { DashboardRepositoryImpl(get()) }

    // ---- ViewModels ----
    factory { SplashScreenModel(get()) }
    factory { HomeScreenModel(get()) }
    factory { LoginScreenModel(get(), get(), get()) }
    factory {  DashboardScreenModel() }
    factory { OtpScreenModel(get(), get(),) }
}
