package com.hybrid.internet.domain.repository.dashboard

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.data.model.response.HomeResponse
import com.hybrid.internet.data.remote.DashboardApi

class DashboardRepositoryImpl(private val api: DashboardApi) : DashboardRepository {
    override suspend fun getDashboard(): NetworkResult<HomeResponse> {
        return blindApiCall { api.fetchDashboard() }

    }


}