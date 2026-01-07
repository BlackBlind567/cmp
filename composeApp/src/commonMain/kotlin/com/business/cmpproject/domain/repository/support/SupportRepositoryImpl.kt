package com.business.cmpproject.domain.repository.support

import com.business.cmpproject.core.network.NetworkResult
import com.business.cmpproject.core.network.blindApiCall
import com.business.cmpproject.data.model.request.LoginRequest
import com.business.cmpproject.data.model.request.SupportRequest
import com.business.cmpproject.data.model.response.LoginResponse
import com.business.cmpproject.data.remote.AuthApi
import com.business.cmpproject.data.remote.SupportApi

class SupportRepositoryImpl (private val api: SupportApi
) : SupportRepository {
    override suspend fun submitReport(request: SupportRequest): NetworkResult<Any> {
        return blindApiCall { api.submitSupportReport(request) }
    }


}