package com.business.cmpproject.core.storage

import com.business.cmpproject.data.model.response.LoginResponse
import com.russhwolf.settings.Settings
import kotlinx.serialization.json.Json

class LocalStorage {

    private val settings = Settings()

    fun saveToken(token: String) {
        settings.putString("token", token)
    }

    fun getToken(): String? {
        return settings.getStringOrNull("token")
    }

    fun saveUser(user: LoginResponse) {
        settings.putString("user", Json.encodeToString(user))
    }
    fun clear() {
        settings.clear()
    }
}