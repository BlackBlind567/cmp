package com.business.cmpproject.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceRequestPaginationResponse(
    @SerialName("data") val requests: List<ServiceRequestItem>,
    @SerialName("current_page") val currentPage: Int,
    @SerialName("last_page") val lastPage: Int
)