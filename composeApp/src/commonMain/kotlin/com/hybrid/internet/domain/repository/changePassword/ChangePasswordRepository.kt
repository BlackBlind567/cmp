package com.hybrid.internet.domain.repository.changePassword

import com.hybrid.internet.core.network.NetworkResult
import com.hybrid.internet.data.model.request.ChangePasswordRequest

interface ChangePasswordRepository {

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest):
            NetworkResult<Int>
}