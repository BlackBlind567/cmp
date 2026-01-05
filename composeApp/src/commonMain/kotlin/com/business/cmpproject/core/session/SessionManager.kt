package com.business.cmpproject.core.session

import com.business.cmpproject.core.network.HttpError
import com.business.cmpproject.core.storage.LocalStorage

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