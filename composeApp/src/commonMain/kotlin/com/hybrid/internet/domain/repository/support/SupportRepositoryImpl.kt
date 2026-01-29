package com.hybrid.internet.domain.repository.support

import SupportRequest
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.data.model.response.SupportTicketResponse
import com.hybrid.internet.data.remote.SupportApi

class SupportRepositoryImpl (private val api: SupportApi
) : SupportRepository {
    override suspend fun submitReport(request: SupportRequest): NetworkResult<SupportTicketResponse> {
        return blindApiCall { api.submitSupportReport(request) }
    }


}