package com.hybrid.internet.domain.repository.ticket

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.data.model.response.TicketPage
import com.hybrid.internet.data.remote.TicketApi

class TicketRepositoryImpl(
    private val api: TicketApi
) : TicketRepository {

    override suspend fun getTicketList(page: Int): NetworkResult<TicketPage> {
        return blindApiCall { api.fetchTicketListFromApi() }
    }

}