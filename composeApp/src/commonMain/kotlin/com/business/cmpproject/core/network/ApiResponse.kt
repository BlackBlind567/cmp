package com.business.cmpproject.core.network

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: Int,
    val success: Boolean,
    val data: T?,
    val message: String
)