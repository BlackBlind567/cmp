package com.business.cmpproject.domain.repository.dashboard

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.network.blindApiCall
import com.business.cmpproject.data.model.response.HomeResponse
import com.business.cmpproject.data.remote.DashboardApi

class DashboardRepositoryImpl(private val api: DashboardApi) : DashboardRepository {
    override suspend fun getDashboard(): NetworkResult<HomeResponse> {
        return blindApiCall { api.fetchDashboard() }

    }


}