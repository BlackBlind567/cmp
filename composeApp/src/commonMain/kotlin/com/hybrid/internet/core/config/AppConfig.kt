package com.hybrid.internet.core.config

object AppConfig {

    // Change once, whole app switches
    val ENVIRONMENT = Environment.PROD

    val BASE_URL: String
        get() = when (ENVIRONMENT) {
//            Environment.PROD -> "https://api.yourapp.com"
            Environment.DEV -> "https://erp.hybridisp.com"
            Environment.UAT -> "https://uat.muftinternet.com"
            Environment.PROD -> "https://erp.hybridisp.com"
        }
}
