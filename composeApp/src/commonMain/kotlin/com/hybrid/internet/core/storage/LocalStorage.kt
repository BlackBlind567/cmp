package com.hybrid.internet.core.storage

import com.hybrid.internet.data.model.response.LoginResponse
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

    fun getUser(): LoginResponse? {
        val userJson = settings.getStringOrNull("user")
        return if (userJson != null) Json.decodeFromString(userJson) else null
    }
    fun clear() {
        settings.clear()
    }
}