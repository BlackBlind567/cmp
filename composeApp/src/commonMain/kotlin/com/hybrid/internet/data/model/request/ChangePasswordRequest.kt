package com.hybrid.internet.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val current_password: String,
    val password: String,
    val password_confirmation: String
)