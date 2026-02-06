package com.hybrid.internet.domain.repository.plan

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.core.session.SessionManager
import com.hybrid.internet.data.model.request.PlanRequest
import com.hybrid.internet.data.model.response.PlanResponse
import com.hybrid.internet.data.model.response.PlanTrackingPaginationResponse
import com.hybrid.internet.data.model.response.PlanUpdateResponse
import com.hybrid.internet.data.remote.PlanApi

class PlanRepositoryImpl(private val api: PlanApi,
    private val sessionManager: SessionManager) : PlanRepository {

    override suspend fun getCustomerPlan(): NetworkResult<List<PlanResponse>> {
        return blindApiCall(sessionManager) { api.fetchPlan() }
    }


    override suspend fun updatePlan(request: PlanRequest): NetworkResult<PlanUpdateResponse> {
        return blindApiCall(sessionManager) { api.updatePlan(request) }

    }

    override suspend fun trackPlan(page: Int): NetworkResult<PlanTrackingPaginationResponse> {
        return blindApiCall(sessionManager) { api.fetchPlanTracking(page) }

    }
}