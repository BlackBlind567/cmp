package com.business.cmpproject.domain.repository.serviceRequest

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.data.model.response.RaiseServiceRequestResponse
import com.business.cmpproject.data.model.response.ServiceRequestPaginationResponse

interface ServiceRequestRepository {
    suspend fun getServiceRequests(page: Int): NetworkResult<ServiceRequestPaginationResponse>

    suspend fun submitServiceRequest(params: MutableMap<String, String>): NetworkResult<RaiseServiceRequestResponse>

}