package com.business.cmpproject.presentation.features.invoice

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.*
import com.business.cmpproject.data.model.response.Invoice
import com.business.cmpproject.presentation.components.AppScaffold

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.business.cmpproject.presentation.components.StandardTopAppBar
import com.business.cmpproject.presentation.theme.CreamBackground
import com.business.cmpproject.presentation.theme.DarkBackground
import kotlinx.coroutines.flow.emptyFlow

data class InvoiceListScreen(
    val invoices: List<Invoice>
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val isDark = isSystemInDarkTheme()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        var searchQuery by remember { mutableStateOf("") }
        val filteredInvoices = remember(searchQuery) {
            if (searchQuery.isEmpty()) invoices
            else invoices.filter {
                it.invoiceNo?.contains(searchQuery, ignoreCase = true) == true
            }
        }

        // Using your custom AppScaffold
        AppScaffold(events = emptyFlow()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(if (isDark) DarkBackground else CreamBackground)
                    .nestedScroll(scrollBehavior.nestedScrollConnection) // Connect scroll logic
            ) {
                // Manually adding the TopBar at the top of the column
                StandardTopAppBar(
                    title = "Invoices",
                    subtitle = "${invoices.size} total",
                    showBack = true, // Explicitly set to true for this screen
                    onBack = { navigator.pop() },
                    scrollBehavior = scrollBehavior
                )

                InvoiceListContent(
                    invoices = filteredInvoices,
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    isDark = isDark
                )
            }
        }
    }
}