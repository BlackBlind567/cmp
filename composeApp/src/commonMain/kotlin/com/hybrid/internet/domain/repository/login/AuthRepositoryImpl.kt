package com.hybrid.internet.domain.repository.login

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.core.session.SessionManager
import com.hybrid.internet.data.model.request.LoginRequest
import com.hybrid.internet.data.model.response.LoginResponse
import com.hybrid.internet.data.remote.AuthApi

class AuthRepositoryImpl(
    private val api: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(
        request: LoginRequest
    ): NetworkResult<LoginResponse> {
        return blindApiCall(sessionManager) { api.login(request) }
    }

    override suspend fun sendOtp(
        mobile: String
    ): NetworkResult<String> {
        return blindApiCall (sessionManager) {
            api.sendOtp(mobile)
        }
    }

    override suspend fun verifyOtp(
        mobile: String,
        otp: String
    ): NetworkResult<LoginResponse> {
        return blindApiCall(sessionManager) { api.verifyOtp(mobile, otp) }
    }

    override suspend fun logout(): NetworkResult<Unit> {
        return blindApiCall(sessionManager) { api.logout() }


    }
}