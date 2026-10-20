package com.hybrid.internet.domain.repository.ticket

import com.hybrid.internet.data.model.request.TicketConfirmationRequest
import com.hybrid.internet.data.model.response.TicketConfirmationResponse

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.data.model.response.TicketMessageItem
import com.hybrid.internet.data.model.response.TicketPage

interface TicketRepository {
    suspend fun confirmTicket(request: TicketConfirmationRequest): NetworkResult<TicketConfirmationResponse>


    suspend fun getTicketList(page: Int): NetworkResult<TicketPage>
    suspend fun getTicketMessages(ticketId: String): NetworkResult<List<TicketMessageItem>>
    suspend fun sendTicketMessage(ticketId: String, message: String, image: String? = null): NetworkResult<Unit>

   /* suspend fun sendOtp(
        mobile: String
    ): NetworkResult<String>*/



    }
