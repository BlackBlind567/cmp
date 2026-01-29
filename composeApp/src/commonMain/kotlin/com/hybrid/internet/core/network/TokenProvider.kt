package com.hybrid.internet.core.network

import com.hybrid.internet.core.storage.LocalStorage


class TokenProvider(
    private val storage: LocalStorage
) {
    fun getToken(): String? = storage.getToken()
}