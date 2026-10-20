package com.hybrid.internet.core.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(

    @SerialName("status")
    val status: Int? = null,

    @SerialName("success")
    val success: Boolean = false,

    @SerialName("message")
    val message: String? = null,

    @SerialName("data")
    val data: T? = null
)