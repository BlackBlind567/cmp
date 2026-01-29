package com.hybrid.internet.presentation.features.support

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hybrid.internet.core.base.encodeBase64
import com.hybrid.internet.core.image.rememberImagePicker
import com.hybrid.internet.core.validation.Validator.mobile
import com.hybrid.internet.data.model.response.PlanResponse
import com.hybrid.internet.data.model.supportmodel.Category
import com.hybrid.internet.data.model.supportmodel.SubCategory
import com.hybrid.internet.presentation.components.AppButton
import com.hybrid.internet.presentation.components.AppTextField
import com.hybrid.internet.presentation.components.DropdownSelector
import com.hybrid.internet.presentation.components.InputType
import com.hybrid.internet.presentation.components.StandardTopAppBar
import com.hybrid.internet.presentation.theme.CreamBackground
import com.hybrid.internet.presentation.theme.DarkBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportContent(
    locationResp: List<PlanResponse>,
    isSubmitting: Boolean = false,
    onSubmit: (Map<String, Any>) -> Unit = {},
    onBack: () -> Unit = {},
    isDark: Boolean,
) {

    // --- 1. DATA SETUP ---
    val categories = remember {
        listOf(
            Category("1", "Network Connectivity Issues", listOf(
                SubCategory("1.1", "Link down/No internet", listOf("Choose Location", "Message")),
                SubCategory("1.2", "Speed Issues", listOf("Choose Location", "Message")),
                SubCategory("1.3", "Packet Loss/Latency", listOf("Choose Location", "Message")),
                SubCategory("1.4", "IP issues", listOf("Choose Location", "IP Address", "Message")),
                SubCategory("1.5", "Wireless Device Issues", listOf("Choose Location", "Message")),
                SubCategory("1.6", "ONU power issues", listOf("Choose Location", "Message")),
                SubCategory("1.7", "General Hardware Issues", listOf("Choose Location", "Message")),
                SubCategory("1.8", "Website/URL issues", listOf("Choose Location", "Write website/URL name", "Message"))
            )),

            Category("2", "Billing & Account Issues", subCategories = emptyList(),
                fields = listOf(
                    "Choose Location",
                    "Invoice Month",
                    "Message"
                )),


            Category("3", "Provisioning/Upgrade/Downgrade/", listOf(
                SubCategory("3.1", "Update on timeline for upgrade", listOf(
                    "Choose Location",
                    "Message"
                )),
                SubCategory("3.2", "Update on timeline for new link", listOf(
                    "Choose Location",
                    "Message"
                )),
                SubCategory("3.3", "Update on other", listOf(
                    "Choose Location",
                    "Message"
                ))
            )),
        )
    }

    val locationsList = remember(locationResp) {
        locationResp.mapNotNull { it.locationName }.distinct()
    }

    /* ---------------- STATE ---------------- */

    val scrollState = rememberScrollState()

    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedSubCategory by remember { mutableStateOf<SubCategory?>(null) }

    val formValues = remember { mutableStateMapOf<String, Any>() }

    var altMobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var activeImageField by remember { mutableStateOf<String?>(null) }

    /* ---------------- IMAGE PICKER ---------------- */

    val picker = rememberImagePicker { bytes ->
        bytes?.let {
            activeImageField?.let { key ->
                formValues[key] = encodeBase64(it)
            }
        }
    }

    /* ---------------- ACTIVE FIELDS ---------------- */

    val activeFields = when {
        selectedSubCategory != null -> selectedSubCategory!!.fields
        selectedCategory != null && selectedCategory!!.subCategories.isEmpty() ->
            selectedCategory!!.fields
        else -> emptyList()
    }

    /* ---------------- VALIDATION ---------------- */

    val isFormValid by derivedStateOf {
        altMobile.isNotBlank() &&
                email.isNotBlank() &&
                activeFields
                    .filterNot { it.contains("Attach", true) }
                    .all { formValues[it]?.toString()?.isNotBlank() == true }
    }

    /* ---------------- UI ---------------- */
    Scaffold(
        containerColor = if (isDark) DarkBackground else CreamBackground,
        topBar = {
            StandardTopAppBar(
                title = "Support Ticket",
                subtitle =  "Add ticket to resolve issue",
                showBack = true,
                onBack = onBack
            )
        }
    ) { padding ->
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {



        /* ---- Contact ---- */

        AppTextField(
            value = altMobile,
            onChange = { input ->
                if (input.length <= 10 && input.all { it.isDigit() }) {
                    altMobile = input
                }
            },
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

        /* ---- Category ---- */

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

        /* ---- Sub Category (Only if exists) ---- */

        if (selectedCategory != null && selectedCategory!!.subCategories.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            DropdownSelector(
                label = "Select Sub-Category",
                options = selectedCategory!!.subCategories.map { it.name },
                selectedOption = selectedSubCategory?.name ?: "",
                onOptionSelected = { name ->
                    selectedSubCategory =
                        selectedCategory!!.subCategories.find { it.name == name }
                    formValues.clear()
                }
            )
        }

        /* ---- Dynamic Fields ---- */

        activeFields.forEach { field ->
            Spacer(Modifier.height(12.dp))

            if (field.contains("Location", true)) {
                DropdownSelector(
                    label = field,
                    options = locationsList,
                    selectedOption = formValues[field]?.toString() ?: "",
                    onOptionSelected = { formValues[field] = it }
                )
            } else {
                OutlinedTextField(
                    value = formValues[field]?.toString() ?: "",
                    onValueChange = { formValues[field] = it },
                    label = { Text(field) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }

        /* ---- IMAGE (ALWAYS VISIBLE, OPTIONAL) ---- */

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                activeImageField = "image"
                picker.pickImage()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (formValues.containsKey("image"))
                    Color(0xFF4CAF50)
                else MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(
                if (formValues.containsKey("image"))
                    Icons.Default.CheckCircle
                else Icons.Default.AddAPhoto,
                null
            )
            Spacer(Modifier.width(8.dp))
            Text(
                if (formValues.containsKey("image"))
                    "Image Attached (Optional)"
                else "Attach Image (Optional)"
            )
        }

        Spacer(Modifier.height(32.dp))

        /* ---- SUBMIT (ALWAYS VISIBLE) ---- */

        AppButton(
            text = "Submit Report",
            enabled = isFormValid,
            loading = isSubmitting,
            onClick = {
                val finalData = mutableMapOf<String, Any>()

                // 1. Add Fixed fields
                finalData["alt_mobile"] = altMobile
                finalData["email"] = email
                finalData["category"] = selectedCategory?.name.orEmpty()
                finalData["sub_category"] = selectedSubCategory?.name.orEmpty()

                // 2. Map Dynamic Fields (Location, Message, or Remark)
                activeFields.forEach { fieldLabel ->
                    val value = formValues[fieldLabel] ?: ""

                    when {
                        fieldLabel.contains("Message", ignoreCase = true) -> {
                            finalData["message"] = value
                        }
                        fieldLabel.contains("Location", ignoreCase = true) -> {
                            finalData["location"] = value
                        }
                        else -> {
                            // This captures "IP Address", "Website/URL", "Invoice Month", etc.
                            finalData["remark"] = value
                        }
                    }
                }

                // 3. Optional Image
                formValues["image"]?.let {
                    finalData["image"] = it
                }

                onSubmit(finalData)
            }
        )


        Spacer(Modifier.height(100.dp))
    }}
}