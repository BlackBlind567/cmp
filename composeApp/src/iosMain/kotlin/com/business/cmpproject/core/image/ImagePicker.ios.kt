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

class IosImagePicker(
    private val onImagePicked: (ByteArray?) -> Unit
) : NSObject(), ImagePicker, UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun pickImage() {
        val picker = UIImagePickerController()
        picker.delegate = this
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypePhotoLibrary

        val controller = UIApplication.sharedApplication.keyWindow?.rootViewController
        controller?.presentViewController(picker, true, null)
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun imagePickerController(picker: UIImagePickerController, didFinishPickingMediaWithInfo: Map<Any?, *>) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        val data = image?.let { UIImageJPEGRepresentation(it, 0.8) }

        val bytes = data?.let { nsData ->
            ByteArray(nsData.length.toInt()).apply {
                usePinned { pinned ->
                    platform.posix.memcpy(pinned.addressOf(0), nsData.bytes, nsData.length)
                }
            }
        }

        onImagePicked(bytes)
        picker.dismissViewControllerAnimated(true, null)
    }
}

@Composable
actual fun rememberImagePicker(onImagePicked: (ByteArray?) -> Unit): ImagePicker {
    return remember { IosImagePicker(onImagePicked) }
}