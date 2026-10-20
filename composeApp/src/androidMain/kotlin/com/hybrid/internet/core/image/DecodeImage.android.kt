package com.hybrid.internet.core.image

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

actual fun decodeImage(bytes: ByteArray): ImageBitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, bounds)
    val options = BitmapFactory.Options().apply { inSampleSize = 1 }
    while (bounds.outWidth / options.inSampleSize > 1600 || bounds.outHeight / options.inSampleSize > 1600) {
        options.inSampleSize *= 2
    }
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)?.asImageBitmap()
}
