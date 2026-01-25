package com.business.cmpproject.core.image

import kotlinx.cinterop.*
import platform.Foundation.*
import platform.UIKit.*
import platform.darwin.NSObject
import platform.posix.memcpy

class ImagePickerDelegate(
    private val onImagePicked: (ByteArray?) -> Unit
) : NSObject(),
    UIImagePickerControllerDelegateProtocol,
    UINavigationControllerDelegateProtocol {

    @OptIn(ExperimentalForeignApi::class)
    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[
            UIImagePickerControllerOriginalImage
        ] as? UIImage

        val data = image?.let { UIImageJPEGRepresentation(it, 0.8) }

        val bytes = data?.let { nsData ->
            ByteArray(nsData.length.toInt()).apply {
                usePinned { pinned ->
                    memcpy(
                        pinned.addressOf(0),
                        nsData.bytes,
                        nsData.length
                    )
                }
            }
        }

        onImagePicked(bytes)
        picker.dismissViewControllerAnimated(true, null)
    }

    override fun imagePickerControllerDidCancel(
        picker: UIImagePickerController
    ) {
        onImagePicked(null)
        picker.dismissViewControllerAnimated(true, null)
    }
}
