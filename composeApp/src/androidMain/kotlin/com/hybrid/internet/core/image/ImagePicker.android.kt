package com.hybrid.internet.core.image

import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

class AndroidImagePicker(
    private val onImagePicked: (ByteArray?) -> Unit,
    private val launcher: androidx.activity.result.ActivityResultLauncher<String>
) : ImagePicker {
    override fun pickImage() {
        launcher.launch("image/*")
    }
}

@Composable
actual fun rememberImagePicker(onImagePicked: (ByteArray?) -> Unit): ImagePicker {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val bytes = uri?.let { context.contentResolver.openInputStream(it)?.readBytes() }
        onImagePicked(bytes)
    }
    return remember { AndroidImagePicker(onImagePicked, launcher) }
}