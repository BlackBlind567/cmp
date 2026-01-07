package com.business.cmpproject.domain.repository.support

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.data.model.request.PlanRequest
import com.business.cmpproject.data.model.request.SupportRequest
import com.business.cmpproject.data.model.response.PlanUpdateResponse

interface SupportRepository {


    suspend fun submitReport(request: SupportRequest): NetworkResult<Any>
}