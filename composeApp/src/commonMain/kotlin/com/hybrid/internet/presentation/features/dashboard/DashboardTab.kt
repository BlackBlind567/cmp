package com.hybrid.internet.presentation.features.dashboard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.material.icons.filled.*
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.hybrid.internet.presentation.features.profile.ProfileScreen

import com.hybrid.internet.presentation.features.home.HomeScreen
import com.hybrid.internet.presentation.features.plans.CustomerPlansScreen
import com.hybrid.internet.presentation.features.serviceRequest.list.ServiceRequestScreen
import com.hybrid.internet.presentation.features.ticket.TicketHistoryList


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
object PlansTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.Receipt)
            return remember { TabOptions(index = 1u, title = "Plan", icon = icon) }
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
    override fun Content() {
        TicketHistoryList().Content()
    }
}

// 4. Plans Tab
object ServicesTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.ListAlt)
            return remember { TabOptions(index = 3u, title = "Service", icon = icon) }
        }

    @Composable
    override fun Content() {
        ServiceRequestScreen().Content()
    }
}

// 5. Profile Tab
object ProfileTab : Tab {
    override val options: TabOptions
        @Composable get() {
            val icon = rememberVectorPainter(Icons.Default.Person)
            return remember { TabOptions(index = 4u, title = "Profile", icon = icon) }
        }

    @Composable
    override fun Content() {
        ProfileScreen().Content()

    }
}