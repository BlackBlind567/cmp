package com.business.cmpproject.core.base

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun encodeBase64(bytes: ByteArray): String {
    return Base64.encode(bytes)
}