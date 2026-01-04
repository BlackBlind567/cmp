package com.business.cmpproject.domain.repository.plan

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.data.model.request.PlanRequest
import com.business.cmpproject.data.model.response.PlanResponse
import com.business.cmpproject.data.model.response.PlanUpdateResponse

interface PlanRepository {

    suspend fun getCustomerPlan(): NetworkResult<List<PlanResponse>>
    suspend fun updatePlan(request: PlanRequest): NetworkResult<PlanUpdateResponse>


}