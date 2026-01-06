package com.business.cmpproject.presentation.features.serviceRequest.add

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.business.cmpproject.presentation.components.AppScaffold

import org.koin.core.parameter.parametersOf
import org.koin.core.parameter.ParametersHolder

class RaiseServiceRequestScreen(val locationId: String = "",
                                val locationName: String = "",
    val onRefresh: () -> Unit = {}
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = getScreenModel<RaiseServiceRequestScreenModel> {
            parametersOf(locationId, locationName)
        }
        val isDark = isSystemInDarkTheme()

        AppScaffold(events = screenModel.events) {
            RaiseServiceRequestContent(
                model = screenModel,
                isDark = isDark,
                onReload = { isSuccess ->
                    if (isSuccess) onRefresh() // Agar success hua toh reload call karo
                    navigator.pop()
                },
                onBack = {
                    navigator.pop()
                }
            )
        }
    }
}