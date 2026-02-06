package com.hybrid.internet.domain.repository.ticket

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.core.session.SessionManager
import com.hybrid.internet.data.model.response.TicketHistoryPaginationResponse
import com.hybrid.internet.data.remote.TicketTrackingApi

class TicketDetailsRepositoryImpl (
    private val api: TicketTrackingApi, private val sessionManager: SessionManager
) : TicketDetailsRepository {
    override suspend fun fetchTicketHistory(
        nextPage: Int,
        extraParams: Map<String, String>
    ): NetworkResult<TicketHistoryPaginationResponse> {
        return blindApiCall(sessionManager) { api.fetchTicketHistory(nextPage, extraParams) }

    }
}