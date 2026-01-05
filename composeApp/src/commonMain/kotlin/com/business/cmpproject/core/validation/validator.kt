package com.business.cmpproject.core.validation

object Validator {

    fun mobile(value: String): String? =
        if (value.length != 10 || !value.all { it.isDigit() })
            "Enter valid mobile number"
        else null

    fun email(value: String): String? =
        when {
            value.isBlank() ->
                "Email is required"

            !EMAIL_REGEX.matches(value) ->
                "Enter valid email address"

            else -> null
        }

    fun password(value: String): String? =
        if (value.length < 6)
            "Password must be at least 6 characters"
        else null

    // 🔒 Centralized email regex (enterprise-safe)
    private val EMAIL_REGEX =
        Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
}
