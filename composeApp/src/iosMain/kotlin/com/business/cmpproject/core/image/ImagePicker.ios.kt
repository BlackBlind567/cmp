package com.business.cmpproject.core.image

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.UIKit.*
import platform.Foundation.*
import platform.darwin.NSObject
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned

import platform.UIKit.*

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

