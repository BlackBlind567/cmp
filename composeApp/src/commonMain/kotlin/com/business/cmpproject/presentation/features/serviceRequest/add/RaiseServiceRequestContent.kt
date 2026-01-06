package com.business.cmpproject.presentation.features.serviceRequest.add

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.business.cmpproject.presentation.components.AppButton
import com.business.cmpproject.presentation.components.AppTextField
import com.business.cmpproject.presentation.components.StandardTopAppBar
import com.business.cmpproject.presentation.theme.CreamBackground
import com.business.cmpproject.presentation.theme.DarkBackground
import com.business.cmpproject.presentation.theme.GreenPrimary
import com.business.cmpproject.presentation.theme.PinkPrimary

//title = "Raise Request",
//subtitle = "Select type and fill details",


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun RaiseServiceRequestContent(
    model: RaiseServiceRequestScreenModel,
    isDark: Boolean,
    onBack: () -> Unit,
    onReload: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    val isLocationPresent = model.locationId.isNotEmpty()

    // Logic: Agar location card se aayi hai toh default "ip_request", warna "new_link"
    LaunchedEffect(Unit) {
        if (isLocationPresent) {
            model.reportType = "ip_request"
        } else {
            model.reportType = "new_link"
        }
    }

    Scaffold(
        containerColor = if (isDark) DarkBackground else CreamBackground,
        topBar = {
            StandardTopAppBar(
                title = "Service Request",
                subtitle = if (isLocationPresent) "Requesting IP for Location" else "Check Feasibility or New Link",
                showBack = true,
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Premium Segmented Toggle ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = if (isDark) Color(0xFF252525) else Color.White,
                border = BorderStroke(1.dp, Color.LightGray.copy(0.2f))
            ) {
                Row(modifier = Modifier.padding(4.dp)) {

                    // 1. IP Request Tab: Sirf tab dikhao jab location ho
                    if (isLocationPresent) {
                        TypeTab(
                            label = "IP Request",
                            isSelected = model.reportType == "ip_request",
                            enabled = false, // Disable taaki switch na kar sake
                            modifier = Modifier.weight(1f)
                        ) { model.reportType = "ip_request" }
                    }

                    // 2. Feasibility Tab
                    TypeTab(
                        label = "Feasibility",
                        isSelected = model.reportType == "feasibility",
                        // Agar location card se aaye ho toh isse disable rakho
                        enabled = !isLocationPresent,
                        modifier = Modifier.weight(1f)
                    ) { model.reportType = "feasibility" }

                    // 3. New Link Tab
                    TypeTab(
                        label = "New Link",
                        isSelected = model.reportType == "new_link",
                        // Agar location card se aaye ho toh isse disable rakho
                        enabled = !isLocationPresent,
                        modifier = Modifier.weight(1f)
                    ) { model.reportType = "new_link" }
                }
            }

            Spacer(Modifier.height(24.dp))

            // --- Location Name Field ---
            AppTextField(
                value = model.locationName,
                onChange = { model.locationName = it },
                label = "Location Name",
                enabled = !isLocationPresent, // Card click par disabled
                error = if (model.locationName.isEmpty()) "Location is required" else null
            )

            // --- Dynamic Form Section ---
            AnimatedContent(
                targetState = model.reportType,
                transitionSpec = { fadeIn() with fadeOut() },
                label = "form_fields"
            ) { type ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    when (type) {
                        "ip_request" -> {
                            AppTextField(value = model.userNameJaze, onChange = { model.userNameJaze = it }, label = "User Name (Jaze)")
                            AppTextField(value = model.reason, onChange = { model.reason = it }, label = "Reason for IP")
                        }
                        "feasibility" -> {
                            AppTextField(value = model.latLong, onChange = { model.latLong = it }, label = "Latitude / Longitude")
                            AppTextField(value = model.btsCode, onChange = { model.btsCode = it }, label = "BTS Code (M6)")
                            AppTextField(value = model.muxId, onChange = { model.muxId = it }, label = "MUX ID (Optional)")
                            AppTextField(value = model.btsAddress, onChange = { model.btsAddress = it }, label = "BTS Address")
                        }
                        "new_link" -> {
                            AppTextField(value = model.bandwidth, onChange = { model.bandwidth = it }, label = "Bandwidth Capacity")
                            AppTextField(value = model.btsCode, onChange = { model.btsCode = it }, label = "BTS Code")
                            AppTextField(value = model.muxId, onChange = { model.muxId = it }, label = "MUX ID (Optional)")
                            AppTextField(value = model.btsAddress, onChange = { model.btsAddress = it }, label = "Installation Address")
                            AppTextField(value = model.note, onChange = { model.note = it }, label = "Special Note")
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            AppTextField(
                value = model.remark,
                onChange = { model.remark = it },
                label = "Additional Remarks (Optional)"
            )

            Spacer(Modifier.height(32.dp))

            AppButton(
                text = "Submit Request",
                loading = model.isSubmitting,
                onClick = { model.submitForm { onReload(true) } }
            )
        }
    }
}

@Composable
fun TypeTab(
    label: String,
    isSelected: Boolean,
    enabled: Boolean = true,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = when {
        !enabled -> Color.Transparent
        isSelected -> if (isDark) PinkPrimary else GreenPrimary
        else -> Color.Transparent
    }
    val contentColor = when {
        !enabled -> Color.Gray.copy(alpha = 0.4f)
        isSelected -> Color.White
        else -> if (isDark) Color.LightGray else Color.Gray
    }

    Surface(
        modifier = modifier.height(44.dp),
        color = bgColor,
        shape = RoundedCornerShape(12.dp),
        onClick = { if (enabled) onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                color = contentColor,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

