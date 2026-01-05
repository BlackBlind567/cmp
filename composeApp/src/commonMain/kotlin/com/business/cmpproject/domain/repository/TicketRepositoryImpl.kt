package com.business.cmpproject.domain.repository

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.network.blindApiCall
import com.business.cmpproject.data.model.request.LoginRequest
import com.business.cmpproject.data.model.response.LoginResponse
import com.business.cmpproject.data.remote.AuthApi
import com.business.cmpproject.data.remote.TicketApi


class TicketRepositoryImpl(
    private val api: TicketApi
) : TicketRepository {

    override suspend fun login(
        request: LoginRequest
    ): NetworkResult<LoginResponse> {
        return blindApiCall { api.login(request) }
    }

    override suspend fun sendOtp(mobile: String): NetworkResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyOtp(
        mobile: String,
        otp: String
    ): NetworkResult<LoginResponse> {
        TODO("Not yet implemented")
    }

}