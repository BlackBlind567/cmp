package com.business.cmpproject.domain.repository

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.network.blindApiCall
import com.business.cmpproject.data.model.request.LoginRequest
import com.business.cmpproject.data.model.response.LoginResponse
import com.business.cmpproject.data.remote.AuthApi

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(
        request: LoginRequest
    ): NetworkResult<LoginResponse> {
        return blindApiCall { api.login(request) }
    }

    override suspend fun sendOtp(
        mobile: String
    ): NetworkResult<String> {
        return blindApiCall {
            api.sendOtp(mobile)
        }
    }

    override suspend fun verifyOtp(
        mobile: String,
        otp: String
    ): NetworkResult<LoginResponse> {
        return blindApiCall {
            api.verifyOtp(mobile, otp)
        }
    }
}