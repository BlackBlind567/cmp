package com.business.cmpproject.presentation.features.support

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.business.cmpproject.core.base.encodeBase64
import com.business.cmpproject.core.image.rememberImagePicker
import com.business.cmpproject.data.model.response.PlanResponse
import com.business.cmpproject.data.model.supportmodel.Category
import com.business.cmpproject.data.model.supportmodel.SubCategory
import com.business.cmpproject.presentation.components.AppButton
import com.business.cmpproject.presentation.components.AppTextField
import com.business.cmpproject.presentation.components.DropdownSelector
import com.business.cmpproject.presentation.components.InputType

@Composable
fun SupportContent(
    locationResp: List<PlanResponse>,
    onSubmit: (Map<String, Any>) -> Unit = {}
) {
    // --- 1. DATA SETUP ---
    val categories = remember {
        listOf(
            Category("1", "Network Connectivity Issues", listOf(
                SubCategory("1.1", "Link down/No internet", listOf("Choose Location", "Attach RX Power Snap", "Remark")),
                SubCategory("1.2", "Speed Issues", listOf("Choose Location", "Attach Speedtest Snap", "Remark")),
                SubCategory("1.3", "Packet Loss/Latency", listOf("Choose Location", "Attach Logs Snap", "Remark")),
                SubCategory("1.4", "IP issues", listOf("Choose Location", "Write IP", "Remark")),
                SubCategory("1.5", "Wireless Device Issues", listOf("Choose Location", "Issue Brief")),
                SubCategory("1.6", "ONU power issues", listOf("Location Name", "Remark")),
                SubCategory("1.7", "General Hardware Issues", listOf("Location Name", "Remark")),
                SubCategory("1.8", "Website/URL issues", listOf("Location Name", "Write website/URL name", "Remark"))
            ))
        )
    }

    val locationsList = remember(locationResp) {
        locationResp.mapNotNull { it.locationName }.distinct()
    }

    // --- 2. STATE MANAGEMENT ---
    val scrollState = rememberScrollState()
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedSubCategory by remember { mutableStateOf<SubCategory?>(null) }
    val formValues = remember { mutableStateMapOf<String, Any>() }

    var altMobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var activeImageField by remember { mutableStateOf<String?>(null) }

    // Native Image Picker Initialization
    val picker = rememberImagePicker { bytes ->
        bytes?.let {
            activeImageField?.let { fieldName ->
                // Encodes to Base64 and stores it in the map
                formValues[fieldName] = encodeBase64(it)
            }
        }
    }

    // --- 3. UI LAYOUT ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState) // Crucial for visibility of bottom items
    ) {
        Text("Report an Issue", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        // Fixed Contact Fields
        AppTextField(
            value = altMobile,
            onChange = { altMobile = it },
            label = "Alternate Mobile",
            inputType = InputType.NUMBER
        )
        Spacer(Modifier.height(8.dp))
        AppTextField(
            value = email,
            onChange = { email = it },
            label = "Email",
            inputType = InputType.EMAIL
        )

        Spacer(Modifier.height(16.dp))

        // Category Dropdown
        DropdownSelector(
            label = "Select Category",
            options = categories.map { it.name },
            selectedOption = selectedCategory?.name ?: "",
            onOptionSelected = { name ->
                selectedCategory = categories.find { it.name == name }
                selectedSubCategory = null
                formValues.clear()
            }
        )

        // Sub-Category Dropdown (Visible only if Category is selected)
        selectedCategory?.let { category ->
            Spacer(Modifier.height(12.dp))
            DropdownSelector(
                label = "Select Sub-Category",
                options = category.subCategories.map { it.name },
                selectedOption = selectedSubCategory?.name ?: "",
                onOptionSelected = { name ->
                    selectedSubCategory = category.subCategories.find { it.name == name }
                    formValues.clear()
                }
            )
        }

        // Dynamic Fields & Submit Button
        selectedSubCategory?.let { sub ->
            Spacer(Modifier.height(24.dp))
            Text("Details", style = MaterialTheme.typography.titleSmall, color = Color.Gray)

            sub.fields.forEach { field ->
                Spacer(Modifier.height(12.dp))

                when {
                    // Location selection
                    field.contains("Location", ignoreCase = true) -> {
                        DropdownSelector(
                            label = field,
                            options = locationsList,
                            selectedOption = formValues[field]?.toString() ?: "",
                            onOptionSelected = { formValues[field] = it }
                        )
                    }

                    // Native Image Picker Button
                    field.contains("Attach", ignoreCase = true) || field.contains("Snap", ignoreCase = true) -> {
                        val isAttached = formValues.containsKey(field)

                        Button(
                            onClick = {
                                activeImageField = field
                                picker.pickImage()
                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAttached) Color(0xFF4CAF50) else MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(if (isAttached) Icons.Default.CheckCircle else Icons.Default.AddAPhoto, null)
                            Spacer(Modifier.width(8.dp))
                            Text(if (isAttached) "Image Attached" else field)
                        }
                    }

                    // Standard Text Input
                    else -> {
                        OutlinedTextField(
                            value = formValues[field]?.toString() ?: "",
                            onValueChange = { formValues[field] = it },
                            label = { Text(field) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            // THE SUBMIT BUTTON
            AppButton(
                text = "Submit Report",
                onClick = {
                    val finalData = buildMap {
                        putAll(formValues) // All text and location data

                        // Extract any Base64 image and assign to 'image' key
                        val base64Image = formValues.values.find { it is String && it.length > 100 }
                        base64Image?.let { put("image", it) }

                        put("alt_mobile", altMobile)
                        put("email", email)
                        put("category", selectedCategory?.name.orEmpty())
                        put("sub_category", sub.name)
                    }
                    onSubmit(finalData)
                }
            )

            // Padding at the bottom to ensure scrollability
            Spacer(Modifier.height(100.dp))
        }
    }
}