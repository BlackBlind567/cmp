package com.hybrid.internet.domain.repository.ticket

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.data.model.response.TicketHistoryPaginationResponse

interface TicketDetailsRepository {

    suspend fun fetchTicketHistory(nextPage: Int, extraParams: Map<String, String>): NetworkResult<TicketHistoryPaginationResponse>
}