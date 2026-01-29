package com.hybrid.internet.domain.repository.plan

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.data.model.request.PlanRequest
import com.hybrid.internet.data.model.response.PlanResponse
import com.hybrid.internet.data.model.response.PlanTrackingPaginationResponse
import com.hybrid.internet.data.model.response.PlanUpdateResponse

interface PlanRepository {

    suspend fun getCustomerPlan(): NetworkResult<List<PlanResponse>>
    suspend fun updatePlan(request: PlanRequest): NetworkResult<PlanUpdateResponse>
    suspend fun trackPlan(page: Int): NetworkResult<PlanTrackingPaginationResponse>
}


