package com.business.cmpproject.presentation.features.support


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.business.cmpproject.data.model.supportmodel.Category
import com.business.cmpproject.data.model.supportmodel.SubCategory

@Composable
fun SupportContent() {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedSubCategory by remember { mutableStateOf<SubCategory?>(null) }

    // formValues mein hum Text ya Image ka URI store karenge
    val formValues = remember { mutableStateMapOf<String, Any>() }


    // Mock Data
    val locationsList = listOf("Noida Office", "Delhi Hub", "Gurgaon Site", "Mumbai DataCenter")
    val categories = listOf(
        Category("1", "Network Connectivity Issues", listOf(
            SubCategory("1.1", "Link down/No internet", listOf("Choose Location", "Attach RX Power Snap", "Remark")),
            SubCategory("1.2", "Speed Issues (Download or Upload)", listOf("Choose Location", "Attach Speedtest snap along with CCR wan Port Snap", "Remark")),
            SubCategory("1.3", "Packet Loss%/ Latency", listOf("Choose Location", "Attach Logs snap with CCR wan port Snap", "Remark")),
            SubCategory("1.4", "IP issues", listOf("Choose Location", "Write IP", "Remark")),
            SubCategory("1.5", "Wireless Device Issues", listOf("Choose Location", "Issue Brief")),
            SubCategory("1.6", "ONU power issues", listOf("Location Name", "Remark")),
            SubCategory("1.7", "General Mikrotik/Hardware Issues", listOf("Location Name", "Remark")),
            SubCategory("1.8", "Website/URL issues", listOf("Location Name", "Write website/URL name", "Remark"))
        ))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Report an Issue", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        // --- Category Selection ---
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

        // --- Sub-Category Selection ---
        if (selectedCategory != null) {
            Spacer(Modifier.height(12.dp))
            DropdownSelector(
                label = "Select Sub-Category",
                options = selectedCategory!!.subCategories.map { it.name },
                selectedOption = selectedSubCategory?.name ?: "",
                onOptionSelected = { name ->
                    selectedSubCategory = selectedCategory!!.subCategories.find { it.name == name }
                    formValues.clear()
                }
            )
        }

        // --- Dynamic Fields ---
        if (selectedSubCategory != null) {
            Spacer(Modifier.height(24.dp))
            Text("Details", style = MaterialTheme.typography.titleSmall, color = Color.Gray)

            selectedSubCategory!!.fields.forEach { field ->
                Spacer(Modifier.height(8.dp))

                when {
                    // Dropdown logic for "Choose Location"
                    field == "Choose Location" -> {
                        DropdownSelector(
                            label = field,
                            options = locationsList,
                            selectedOption = formValues[field]?.toString() ?: "",
                            onOptionSelected = { formValues[field] = it }
                        )
                    }

                    // Button logic for "Attach"
                    field.contains("Attach", ignoreCase = true) -> {
                        val hasFile = formValues.containsKey(field)
                        OutlinedButton(
                            onClick = {

                            },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = if (hasFile) ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFE8F5E9))
                            else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(if (hasFile) Icons.Default.CheckCircle else Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = if (hasFile) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary)
                                Spacer(Modifier.width(8.dp))
                                Text(if (hasFile) "File Attached" else field)
                            }
                        }
                    }

                    // Text field for everything else (including Location Name)
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

            Button(
                onClick = { /* Submit logic: formValues contains all data */ },
                modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Submit Report")
            }
        }
    }


}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
            shape = RoundedCornerShape(8.dp)
        )
        Box(modifier = Modifier.matchParentSize().clickable { expanded = !expanded })

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}