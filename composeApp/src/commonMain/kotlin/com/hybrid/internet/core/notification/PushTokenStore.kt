package com.hybrid.internet.core.notification

import com.hybrid.internet.core.storage.LocalStorage

fun savePushToken(token: String) {
    LocalStorage().savePushToken(token)
}

fun getPushToken(): String? {
    return LocalStorage().getPushToken()
}
