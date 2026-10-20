package com.hybrid.internet.core.base

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun encodeBase64(bytes: ByteArray): String {
    return Base64.encode(bytes)
}

fun encodeImageDataUri(bytes: ByteArray): String {
    fun startsWith(vararg signature: Int): Boolean =
        bytes.size >= signature.size && signature.indices.all { (bytes[it].toInt() and 0xff) == signature[it] }

    val mimeType = when {
        startsWith(0xff, 0xd8, 0xff) -> "image/jpeg"
        startsWith(0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a) -> "image/png"
        startsWith(0x47, 0x49, 0x46, 0x38) -> "image/gif"
        startsWith(0x52, 0x49, 0x46, 0x46) && bytes.size >= 12 &&
            bytes.copyOfRange(8, 12).decodeToString() == "WEBP" -> "image/webp"
        startsWith(0x42, 0x4d) -> "image/bmp"
        bytes.size >= 12 && bytes.copyOfRange(4, 8).decodeToString() == "ftyp" -> {
            when (bytes.copyOfRange(8, 12).decodeToString()) {
                "heic", "heix", "hevc", "hevx" -> "image/heic"
                "mif1", "msf1" -> "image/heif"
                "avif", "avis" -> "image/avif"
                else -> throw IllegalArgumentException("Please select a JPEG, PNG or WebP photo.")
            }
        }
        else -> throw IllegalArgumentException("Please select a JPEG, PNG or WebP photo.")
    }
    return "data:$mimeType;base64,${encodeBase64(bytes)}"
}
