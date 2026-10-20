package com.hybrid.internet.core.image

import androidx.compose.ui.graphics.ImageBitmap

expect fun decodeImage(bytes: ByteArray): ImageBitmap?
