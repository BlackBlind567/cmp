package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketHistoryPaginationResponse(
    val data: List<TicketHistoryItem>,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("last_page") val lastPage: Int
)