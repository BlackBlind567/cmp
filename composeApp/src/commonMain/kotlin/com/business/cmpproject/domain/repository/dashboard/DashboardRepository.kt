package com.business.cmpproject.domain.repository.dashboard

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.data.model.response.HomeResponse

interface DashboardRepository {
    suspend fun getDashboard(): NetworkResult<HomeResponse>
}