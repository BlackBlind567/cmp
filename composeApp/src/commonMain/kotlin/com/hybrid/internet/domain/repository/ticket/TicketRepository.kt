package com.hybrid.internet.domain.repository.ticket

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.data.model.response.TicketPage

interface TicketRepository {


    suspend fun getTicketList(page: Int): NetworkResult<TicketPage>

   /* suspend fun sendOtp(
        mobile: String
    ): NetworkResult<String>*/



    }