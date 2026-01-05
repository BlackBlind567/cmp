package com.business.cmpproject.presentation.features.ticket

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator

class TicketHistoryScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<TicketScreenModel>()
        val navigator = LocalNavigator.current!!
        TicketHistoryContent(viewModel)
    }
}