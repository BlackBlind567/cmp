package com.hybrid.internet.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanTrackingPaginationResponse(
    val data: List<PlanRequestItem>,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("last_page") val lastPage: Int,
    @SerialName("next_page_url") val nextPageUrl: String?
)