package com.hybrid.internet.domain.repository.plan

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.data.model.request.PlanRequest
import com.hybrid.internet.data.model.response.PlanResponse
import com.hybrid.internet.data.model.response.PlanTrackingPaginationResponse
import com.hybrid.internet.data.model.response.PlanUpdateResponse
import com.hybrid.internet.data.remote.PlanApi

class PlanRepositoryImpl(private val api: PlanApi) : PlanRepository {

    override suspend fun getCustomerPlan(): NetworkResult<List<PlanResponse>> {
        return blindApiCall { api.fetchPlan() }
    }


    override suspend fun updatePlan(request: PlanRequest): NetworkResult<PlanUpdateResponse> {
        return blindApiCall { api.updatePlan(request) }

    }

    override suspend fun trackPlan(page: Int): NetworkResult<PlanTrackingPaginationResponse> {
        return blindApiCall { api.fetchPlanTracking(page) }

    }
}