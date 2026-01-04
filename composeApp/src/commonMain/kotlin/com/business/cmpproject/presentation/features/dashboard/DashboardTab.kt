package com.business.cmpproject.presentation.features.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Text
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

import com.business.cmpproject.presentation.features.home.HomeScreen
import com.business.cmpproject.presentation.features.plans.CustomerPlansScreen


object HomeTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.Dashboard)
            return remember { TabOptions(index = 0u, title = "Home", icon = icon) }
        }

    @Composable
    override fun Content() {
        // Call your Dashboard Screen here
        HomeScreen().Content()
    }
}

// 2. Invoices Tab
object InvoicesTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.Receipt)
            return remember { TabOptions(index = 1u, title = "Pricing", icon = icon) }
        }

    @Composable
    override fun Content() {
        CustomerPlansScreen().Content()
    }
}

// 3. Tickets Tab
object TicketsTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.ConfirmationNumber)
            return remember { TabOptions(index = 2u, title = "Tickets", icon = icon) }
        }

    @Composable
    override fun Content() { Text("Support Tickets Screen") }
}

// 4. Plans Tab
object PlansTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.ListAlt)
            return remember { TabOptions(index = 3u, title = "Plans", icon = icon) }
        }

    @Composable
    override fun Content() { Text("Active Plans Screen") }
}

// 5. Profile Tab
object ProfileTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.Person)
            return remember { TabOptions(index = 4u, title = "Profile", icon = icon) }
        }

    @Composable
    override fun Content() { Text("Profile Settings Screen") }
}