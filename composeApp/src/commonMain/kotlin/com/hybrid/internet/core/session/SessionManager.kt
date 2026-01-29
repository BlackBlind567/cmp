package com.hybrid.internet.core.session

import com.hybrid.internet.core.network.HttpError
import com.hybrid.internet.core.storage.LocalStorage

class SessionManager(
    private val storage: LocalStorage
) {

    fun handle(error: HttpError): Boolean {
        return if (error is HttpError.Unauthorized) {
            storage.clear()
            true // session expired
        } else {
            false
        }
    }
}