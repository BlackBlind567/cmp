package com.business.cmpproject.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OtpInput(
    otp: String,
    onOtpChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val otpLength = 6

    val focusRequesters = remember {
        List(otpLength) { FocusRequester() }
    }

    var focusedIndex by remember { mutableStateOf(0) }

    val chars = List(otpLength) { index ->
        otp.getOrNull(index)?.toString() ?: ""
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        chars.forEachIndexed { index, char ->

            BasicTextField(
                value = char,
                onValueChange = { value ->
                    if (value.length <= 1 && value.all { it.isDigit() }) {

                        val newOtp = chars.toMutableList()
                        newOtp[index] = value
                        onOtpChange(newOtp.joinToString(""))

                        if (value.isNotEmpty() && index < otpLength - 1) {
                            focusRequesters[index + 1].requestFocus()
                        }
                        if (value.isEmpty() && index > 0) {
                            focusRequesters[index - 1].requestFocus()
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .focusRequester(focusRequesters[index])
                    .onFocusChanged {
                        if (it.isFocused) focusedIndex = index
                    }
                    .border(
                        width = if (focusedIndex == index) 2.dp else 1.dp,
                        color =
                            if (focusedIndex == index)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(vertical = 10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                ),
//                cursorBrush = SolidColor(MaterialTheme.colorScheme.transparent) // 🔥 hide cursor
            )
        }
    }

    // Auto focus first box
    LaunchedEffect(Unit) {
        focusRequesters.first().requestFocus()
    }
}
