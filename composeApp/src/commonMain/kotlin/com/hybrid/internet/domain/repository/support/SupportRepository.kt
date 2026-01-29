package com.hybrid.internet.domain.repository.support

import SupportRequest
import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.data.model.response.SupportTicketResponse

interface SupportRepository {


    suspend fun submitReport(request: SupportRequest): NetworkResult<SupportTicketResponse>
}