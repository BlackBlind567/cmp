package com.hybrid.internet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

