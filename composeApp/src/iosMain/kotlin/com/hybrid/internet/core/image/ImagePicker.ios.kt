package com.hybrid.internet.core.image

import platform.UIKit.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class IosImagePicker(
    private val onImagePicked: (ByteArray?) -> Unit
) : ImagePicker {

    private val delegate = ImagePickerDelegate(onImagePicked)

    override fun pickImage() {
        val picker = UIImagePickerController()
        picker.delegate = delegate
        picker.sourceType =
            UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary

        val controller =
            UIApplication.sharedApplication.keyWindow?.rootViewController

        controller?.presentViewController(picker, true, null)
    }
}

@Composable
actual fun rememberImagePicker(
    onImagePicked: (ByteArray?) -> Unit
): ImagePicker {
    return remember { IosImagePicker(onImagePicked) }
}

