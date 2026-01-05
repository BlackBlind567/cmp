package com.business.cmpproject.di

import com.business.cmpproject.core.dispatchers.AppDispatchers
import com.business.cmpproject.core.network.ApiClient
import com.business.cmpproject.core.network.TokenProvider
import com.business.cmpproject.core.session.SessionManager
import com.business.cmpproject.core.storage.LocalStorage
import com.business.cmpproject.data.remote.AuthApi
import com.business.cmpproject.data.remote.TicketApi
import com.business.cmpproject.domain.repository.AuthRepository
import com.business.cmpproject.domain.repository.AuthRepositoryImpl
import com.business.cmpproject.domain.repository.TicketRepository
import com.business.cmpproject.domain.repository.TicketRepositoryImpl
import com.business.cmpproject.presentation.features.dashboard.DashboardScreenModel
import com.business.cmpproject.presentation.features.login.LoginScreenModel
import com.business.cmpproject.presentation.features.otp.OtpScreenModel
import com.business.cmpproject.presentation.features.splash.SplashScreenModel
import com.business.cmpproject.presentation.features.ticket.TicketScreenModel
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
    single { TicketApi(get()) }                    // HttpClient injected

    // ---- Repository ----
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<TicketRepository> { TicketRepositoryImpl(get()) }

    // ---- ViewModels ----
    factory { SplashScreenModel(get()) }
    factory { LoginScreenModel(get(), get(), get()) }
    factory { DashboardScreenModel() }
    factory { OtpScreenModel(get(), get(),) }
    factory { TicketScreenModel(get(), get()) }
}
