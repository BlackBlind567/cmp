package com.hybrid.internet.presentation.features.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hybrid.internet.data.model.response.Invoice

@Composable
fun InvoiceRow(
    invoice: Invoice,
    onClick: () -> Unit
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 1.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = invoice.invoiceNo!!,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = invoice.invoiceDate!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "₹${invoice.netAmount!!}",
                    style = MaterialTheme.typography.titleSmall
                )
                StatusChip(invoice.paymentStatus!!)
            }
        }
    }
}
