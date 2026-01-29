package com.hybrid.internet.domain.repository.dashboard

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.data.model.response.HomeResponse

interface DashboardRepository {
    suspend fun getDashboard(): NetworkResult<HomeResponse>
}