package com.hybrid.internet.presentation.features.invoice

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hybrid.internet.data.model.response.Invoice
import com.hybrid.internet.presentation.features.home.EmptyStateCard
import com.hybrid.internet.presentation.features.home.PremiumInvoiceRow
import com.hybrid.internet.presentation.theme.CreamSurface
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.DarkTextPrimary
import com.hybrid.internet.presentation.theme.DarkTextSecondary
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.LightTextPrimary
import com.hybrid.internet.presentation.theme.LightTextSecondary
import com.hybrid.internet.presentation.theme.PinkPrimary

@Composable
fun InvoiceListContent(
    invoices: List<Invoice>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    isDark: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // --- Search Bar ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Search by Invoice No...",
                    color = if (isDark) DarkTextSecondary else LightTextSecondary
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = if (isDark) PinkPrimary else GreenPrimary
                )
            },
            shape = RoundedCornerShape(16.dp),
            // --- LATEST MATERIAL 3 API ---
            colors = OutlinedTextFieldDefaults.colors(
                // Border Colors
                focusedBorderColor = if (isDark) PinkPrimary else GreenPrimary,
                unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f),

                // Container (Background) Colors
                focusedContainerColor = if (isDark) DarkSurface else CreamSurface,
                unfocusedContainerColor = if (isDark) DarkSurface else CreamSurface,

                // Text Colors
                focusedTextColor = if (isDark) DarkTextPrimary else LightTextPrimary,
                unfocusedTextColor = if (isDark) DarkTextPrimary else LightTextPrimary,

                // Cursor Color
                cursorColor = if (isDark) PinkPrimary else GreenPrimary
            )
        )
        Spacer(Modifier.height(20.dp))

        // --- Invoices List ---
        if (invoices.isEmpty()) {
            EmptyStateCard("No invoices found matching your search")
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                items(invoices, key = { it.id!! }) { invoice ->
                    // Reusing the Premium Row we created for Home
                    PremiumInvoiceRow(
                        invoice = invoice,
                        isDark = isDark,
                        onClick = {
                            // Logic to open PDF or Details
                            // openUrl(invoice.pdfFile)
                        }
                    )
                }
            }
        }
    }
}