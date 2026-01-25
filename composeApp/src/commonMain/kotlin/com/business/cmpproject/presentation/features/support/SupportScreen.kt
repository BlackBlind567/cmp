package com.business.cmpproject.presentation.features.support

import SupportRequest
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.data.model.response.PlanResponse
import com.business.cmpproject.data.model.supportmodel.*
import com.business.cmpproject.presentation.components.*
import com.business.cmpproject.presentation.features.plans.CustomerPlansScreenModel
import com.business.cmpproject.presentation.theme.GreenPrimary
import com.business.cmpproject.presentation.theme.PinkPrimary

class SupportScreen : Screen {

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<SupportScreenModel>()
        val plansModel = getScreenModel<CustomerPlansScreenModel>()
        val isDark = isSystemInDarkTheme()
        val planState by plansModel.state.collectAsState()
        val submitState by screenModel.state.collectAsState()

        when (planState) {
            is UiState.Loading -> { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = if (isDark) PinkPrimary else GreenPrimary)
            }}
            is UiState.Success -> {
                val planData = (planState as UiState.Success<List<PlanResponse>>).data
                SupportContent(
                    locationResp = planData,
                    onSubmit = { payload ->
                        // 💡 FIX: Map the payload values to your SupportRequest
                        screenModel.submitReport(
                            req = SupportRequest(
                                altMobile = payload["alt_mobile"]?.toString() ?: "",
                                altEmail = payload["email"]?.toString() ?: "",
                                category = payload["category"]?.toString() ?: "",
                                subCategory = payload["sub_category"]?.toString() ?: "",
                                location = payload["Choose Location"]?.toString() ?: payload["Location Name"]?.toString() ?: "",
                                remark = payload["Remark"]?.toString() ?: "",
                                message = payload["Issue Brief"]?.toString() ?: "",
                                image = payload["image"]?.toString() ?: ""
                            )
                        )
                    }
                )
            }
            else -> Text("Unable to load data")
        }
    }
}
