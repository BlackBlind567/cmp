package com.hybrid.internet.domain.repository.changePassword

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.core.network.blindApiCall
import com.hybrid.internet.core.session.SessionManager
import com.hybrid.internet.data.model.request.ChangePasswordRequest
import com.hybrid.internet.data.remote.AuthApi

class ChangePasswordRepositoryImpl(
    private val api: AuthApi,
    private val sessionManager: SessionManager
): ChangePasswordRepository {
    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): NetworkResult<Int> {
        return blindApiCall(
            sessionManager = sessionManager
        ) {
            api.changePassword(changePasswordRequest)
        }
    }
}