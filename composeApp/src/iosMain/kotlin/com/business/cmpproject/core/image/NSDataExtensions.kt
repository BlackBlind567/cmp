package com.business.cmpproject.core.image

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.posix.memcpy

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    return ByteArray(length.toInt()).apply {
        memScoped {
            memcpy(
                this@apply.refTo(0),
                this@toByteArray.bytes,
                this@toByteArray.length
            )
        }
    }
}