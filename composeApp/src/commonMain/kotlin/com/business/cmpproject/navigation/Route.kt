package com.business.cmpproject.navigation

sealed class Route(val path: String) {
    object Splash : Route("splash")
    object Login : Route("login")
    object Dashboard : Route("dashboard")

}
