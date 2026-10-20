package com.hybrid.internet.presentation.features.payment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.composeapp.generated.resources.Res
import com.hybrid.internet.composeapp.generated.resources.upi_qr
import com.hybrid.internet.presentation.components.AppScaffold
import com.hybrid.internet.presentation.components.StandardTopAppBar
import com.hybrid.internet.presentation.theme.CreamBackground
import com.hybrid.internet.presentation.theme.CreamSurface
import com.hybrid.internet.presentation.theme.DarkBackground
import com.hybrid.internet.presentation.theme.DarkSurface
import com.hybrid.internet.presentation.theme.DarkTextPrimary
import com.hybrid.internet.presentation.theme.GreenPrimary
import com.hybrid.internet.presentation.theme.LightTextPrimary
import com.hybrid.internet.presentation.theme.LightTextSecondary
import com.hybrid.internet.presentation.theme.PinkPrimary
import kotlinx.coroutines.flow.emptyFlow
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs
import kotlin.math.round

data class PaymentDetailsScreen(
    val dueAmount: Double
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val isDark = isSystemInDarkTheme()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        AppScaffold(events = emptyFlow()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDark) DarkBackground else CreamBackground)
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                StandardTopAppBar(
                    title = "Payment Details",
                    subtitle = "Outstanding ${formatAmount(dueAmount)}",
                    showBack = true,
                    onBack = { navigator.pop() },
                    scrollBehavior = scrollBehavior,
                    isTitleCenter = false
                )

                PaymentDetailsContent(
                    dueAmount = dueAmount,
                    isDark = isDark
                )
            }
        }
    }
}

@Composable
private fun PaymentDetailsContent(
    dueAmount: Double,
    isDark: Boolean
) {
    val accent = if (isDark) PinkPrimary else GreenPrimary
    val surface = if (isDark) DarkSurface else CreamSurface
    val mainText = if (isDark) DarkTextPrimary else LightTextPrimary
    val secondaryText = if (isDark) Color.LightGray else LightTextSecondary

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Amount Due",
                        color = secondaryText,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = formatAmount(dueAmount),
                        color = accent,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = accent
                        )
                        Text(
                            text = "Bank Details",
                            modifier = Modifier.padding(start = 8.dp),
                            color = mainText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    BankDetailRow("Bank Name", "Kotak Mahindra Bank", mainText, secondaryText)
                    BankDetailRow("Beneficiary Name", "Hyosec Solutions Private Limited", mainText, secondaryText)
                    BankDetailRow("Account Number", "6512944573", mainText, secondaryText)
                    BankDetailRow("IFSC", "KKBK0001350", mainText, secondaryText)
                    BankDetailRow("Branch Name", "New Marine Lines Br, Mumbai, 400020", mainText, secondaryText)
                    BankDetailRow("UPI", "HYOSECC@KOTAK", mainText, secondaryText)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = null,
                            tint = accent
                        )
                        Text(
                            text = "UPI QR",
                            modifier = Modifier.padding(start = 8.dp),
                            color = mainText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Image(
                        painter = painterResource(Res.drawable.upi_qr),
                        contentDescription = "UPI QR code",
                        modifier = Modifier.size(260.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        item {
            OutlinedButton(
                onClick = {},
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Pay Now by Payment Gateway")
            }
        }
    }
}

@Composable
private fun BankDetailRow(
    label: String,
    value: String,
    mainText: Color,
    secondaryText: Color
) {
    Column(modifier = Modifier.padding(vertical = 7.dp)) {
        Text(
            text = label,
            color = secondaryText,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = value,
            color = mainText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatAmount(amount: Double): String {
    val cents = round(amount * 100).toLong()
    val sign = if (cents < 0) "-" else ""
    val absoluteCents = abs(cents)
    val wholeAmount = absoluteCents / 100
    val decimalAmount = (absoluteCents % 100).toString().padStart(2, '0')
    return "Rs. $sign$wholeAmount.$decimalAmount"
}
