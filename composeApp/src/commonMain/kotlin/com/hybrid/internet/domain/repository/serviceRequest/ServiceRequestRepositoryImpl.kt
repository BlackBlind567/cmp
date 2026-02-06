package com.hybrid.internet.domain.repository.serviceRequest

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.core.session.SessionManager
import com.hybrid.internet.data.model.response.RaiseServiceRequestResponse
import com.hybrid.internet.data.model.response.ServiceRequestPaginationResponse
import com.hybrid.internet.data.remote.ServiceRequestApi

class ServiceRequestRepositoryImpl(private val api: ServiceRequestApi,
    private val sessionManager: SessionManager) : ServiceRequestRepository {
    override suspend fun getServiceRequests(page: Int): NetworkResult<ServiceRequestPaginationResponse> {
       return blindApiCall(sessionManager) { api.getServiceRequests(page) }

    }

    override suspend fun submitServiceRequest(params: MutableMap<String, String>): NetworkResult<RaiseServiceRequestResponse> {
        return blindApiCall(sessionManager) { api.submitRequest(params) }

    }
}