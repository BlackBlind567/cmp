package com.hybrid.internet.core.config

object AppConfig {

    // Change once, whole app switches
    val ENVIRONMENT = Environment.DEV

    val BASE_URL: String
        get() = when (ENVIRONMENT) {
//            Environment.DEV -> "https://uat.muftinternet.com"
//            Environment.UAT -> "https://erp.hybridisp.com"
//            Environment.PROD -> "https://api.yourapp.com"
            Environment.DEV -> "https://erp.hybridisp.com"
            Environment.UAT -> "https://erp.hybridisp.com"
            Environment.PROD -> "https://erp.hybridisp.com"
        }
}