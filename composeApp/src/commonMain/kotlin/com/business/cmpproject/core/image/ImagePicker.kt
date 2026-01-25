package com.business.cmpproject.core.image

import androidx.compose.runtime.Composable

interface ImagePicker {
    fun pickImage()
}

@Composable
expect fun rememberImagePicker(onImagePicked: (ByteArray?) -> Unit): ImagePicker
