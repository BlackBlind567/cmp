package com.hybrid.internet.domain.repository.serviceRequest

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.data.model.response.RaiseServiceRequestResponse
import com.hybrid.internet.data.model.response.ServiceRequestPaginationResponse

interface ServiceRequestRepository {
    suspend fun getServiceRequests(page: Int): NetworkResult<ServiceRequestPaginationResponse>

    suspend fun submitServiceRequest(params: MutableMap<String, String>): NetworkResult<RaiseServiceRequestResponse>

}