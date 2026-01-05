package com.business.cmpproject.presentation.features.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DashboardScreenModel : ScreenModel {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.TabSelected -> {
                _state.update {
                    it.copy(selectedTab = event.tab)
                }
            }
        }
    }
}