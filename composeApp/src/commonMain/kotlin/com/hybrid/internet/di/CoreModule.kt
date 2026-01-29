package com.hybrid.internet.di

import com.hybrid.internet.core.dispatchers.AppDispatchers
import com.hybrid.internet.core.network.ApiClient
import com.hybrid.internet.core.network.TokenProvider
import com.hybrid.internet.core.session.SessionManager
import com.hybrid.internet.core.storage.LocalStorage
import com.hybrid.internet.data.remote.AuthApi
import com.hybrid.internet.data.remote.DashboardApi
import com.hybrid.internet.data.remote.PlanApi
import com.hybrid.internet.data.remote.ServiceRequestApi
import com.hybrid.internet.data.remote.SupportApi
import com.hybrid.internet.data.remote.TicketApi
import com.hybrid.internet.data.remote.TicketTrackingApi
import com.hybrid.internet.domain.repository.login.AuthRepository
import com.hybrid.internet.domain.repository.login.AuthRepositoryImpl
import com.hybrid.internet.domain.repository.dashboard.DashboardRepository
import com.hybrid.internet.domain.repository.dashboard.DashboardRepositoryImpl
import com.hybrid.internet.domain.repository.plan.PlanRepository
import com.hybrid.internet.domain.repository.plan.PlanRepositoryImpl
import com.hybrid.internet.domain.repository.serviceRequest.ServiceRequestRepository
import com.hybrid.internet.domain.repository.serviceRequest.ServiceRequestRepositoryImpl
import com.hybrid.internet.domain.repository.support.SupportRepository
import com.hybrid.internet.domain.repository.support.SupportRepositoryImpl
import com.hybrid.internet.domain.repository.ticket.TicketDetailsRepository
import com.hybrid.internet.domain.repository.ticket.TicketDetailsRepositoryImpl
import com.hybrid.internet.domain.repository.ticket.TicketRepository
import com.hybrid.internet.domain.repository.ticket.TicketRepositoryImpl
import com.hybrid.internet.presentation.features.profile.ProfileScreenModel
import com.hybrid.internet.presentation.features.home.HomeScreenModel
import com.hybrid.internet.presentation.features.login.LoginScreenModel
import com.hybrid.internet.presentation.features.otp.OtpScreenModel
import com.hybrid.internet.presentation.features.plans.CustomerPlansScreenModel
import com.hybrid.internet.presentation.features.serviceRequest.add.RaiseServiceRequestScreenModel
import com.hybrid.internet.presentation.features.serviceRequest.list.ServiceRequestScreenModel
import com.hybrid.internet.presentation.features.splash.SplashScreenModel
import com.hybrid.internet.presentation.features.statusTracking.PlanTrackingScreenModel
import com.hybrid.internet.presentation.features.support.SupportScreenModel
import com.hybrid.internet.presentation.features.ticket.TicketScreenModel
import com.hybrid.internet.presentation.features.ticketHistory.TicketHistoryScreenModel
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
    single { PlanApi(get()) }                    // HttpClient injected
    single { TicketTrackingApi(get()) }                    // HttpClient injected
    single { TicketApi(get()) }                    // HttpClient injected
    single { ServiceRequestApi(get()) }                    // HttpClient injected
    single { SupportApi(get()) }                    // HttpClient injected

    // ---- Repository ----
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<DashboardRepository> { DashboardRepositoryImpl(get()) }
    single<TicketDetailsRepository> { TicketDetailsRepositoryImpl(get()) }
    single<TicketRepository> { TicketRepositoryImpl(get()) }
    single<PlanRepository> { PlanRepositoryImpl(get()) }
    single<ServiceRequestRepository> { ServiceRequestRepositoryImpl(get()) }
    single<SupportRepository> { SupportRepositoryImpl(get()) }

    // ---- ViewModels ----
    factory { SplashScreenModel(get()) }
    factory { HomeScreenModel(get(), get()) }
    factory { CustomerPlansScreenModel(get()) }
    factory { PlanTrackingScreenModel(get()) }
    factory { ProfileScreenModel(get()) }
    factory { ServiceRequestScreenModel(get()) }

// In your Koin Module
    factory { (locId: String, locName: String) ->
        RaiseServiceRequestScreenModel(
            repo = get(),
            initialLocationId = locId,
            initialLocationName = locName
        )
    }
    factory { LoginScreenModel(get(), get(), get()) }
    factory { (ticketId: Int) -> TicketHistoryScreenModel(get(), ticketId) }
    factory { OtpScreenModel(get(), get(),) }
    factory { TicketScreenModel(get(), get(),) }
    factory { SupportScreenModel(get(),get()) }
}
