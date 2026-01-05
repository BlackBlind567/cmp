package com.business.cmpproject.core.network

import com.business.cmpproject.core.storage.LocalStorage


class TokenProvider(
    private val storage: LocalStorage
) {
    fun getToken(): String? = storage.getToken()
}