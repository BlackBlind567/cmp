package com.business.cmpproject.domain.repository.login

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.data.model.request.LoginRequest
import com.business.cmpproject.data.model.response.LoginResponse


interface AuthRepository {
    suspend fun login(request: LoginRequest): NetworkResult<LoginResponse>

    suspend fun sendOtp(
        mobile: String
    ): NetworkResult<String>

    suspend fun verifyOtp(
        mobile: String,
        otp: String
    ): NetworkResult<LoginResponse>
}