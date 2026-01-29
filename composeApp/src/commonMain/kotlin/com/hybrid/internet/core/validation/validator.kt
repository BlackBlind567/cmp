package com.hybrid.internet.core.validation

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

    fun strongPassword(value: String): String? {
        if (value.length < 6)
            return "Password must be at least 6 characters"

        if (!value.any { it.isUpperCase() })
            return "Password must contain at least 1 uppercase letter"

        if (!value.any { it.isLowerCase() })
            return "Password must contain at least 1 lowercase letter"

        if (!value.any { it.isDigit() })
            return "Password must contain at least 1 number"

        if (!value.any { "!@#$%^&*()_+-=[]{}|;:',.<>?/".contains(it) })
            return "Password must contain at least 1 special character"

        return null
    }

    fun confirmPassword(password: String, confirm: String): String? =
        if (password != confirm) "Password confirmation does not match"
        else null

}
