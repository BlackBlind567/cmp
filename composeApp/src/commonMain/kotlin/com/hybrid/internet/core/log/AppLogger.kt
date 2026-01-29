package com.hybrid.internet.core.log

object AppLogger {

    var enabled = true // disable in PROD if needed

    fun d(tag: String, message: String) {
        if (enabled) println("DEBUG [$tag] $message")
    }

    fun e(tag: String, message: String) {
        println("ERROR [$tag] $message")
    }

    fun network(message: String) {
        if (enabled) println("NETWORK $message")
    }

    fun log(message: String) {
        println(message) // works on Android + iOS
    }
}