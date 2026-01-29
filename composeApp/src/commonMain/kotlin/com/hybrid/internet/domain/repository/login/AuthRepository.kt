package com.hybrid.internet.domain.repository.login

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.data.model.request.LoginRequest
import com.hybrid.internet.data.model.response.LoginResponse


interface AuthRepository {
    suspend fun login(request: LoginRequest): NetworkResult<LoginResponse>

    suspend fun sendOtp(
        mobile: String
    ): NetworkResult<String>

    suspend fun verifyOtp(
        mobile: String,
        otp: String
    ): NetworkResult<LoginResponse>

    suspend fun logout(): NetworkResult<Unit>



}