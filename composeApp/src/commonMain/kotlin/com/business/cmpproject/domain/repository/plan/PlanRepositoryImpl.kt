package com.business.cmpproject.domain.repository.plan

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.network.blindApiCall
import com.business.cmpproject.data.model.request.PlanRequest
import com.business.cmpproject.data.model.response.PlanResponse
import com.business.cmpproject.data.model.response.PlanUpdateResponse
import com.business.cmpproject.data.remote.PlanApi

class PlanRepositoryImpl(private val api: PlanApi) : PlanRepository {

    override suspend fun getCustomerPlan(): NetworkResult<List<PlanResponse>> {
        return blindApiCall { api.fetchPlan() }
    }


    override suspend fun updatePlan(request: PlanRequest): NetworkResult<PlanUpdateResponse> {
        return blindApiCall { api.updatePlan(request) }

    }
}