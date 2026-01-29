import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hybrid.internet.core.state.UiState
import com.hybrid.internet.data.model.response.PlanResponse
import com.hybrid.internet.presentation.features.home.HomeScreen
import com.hybrid.internet.presentation.features.plans.CustomerPlansScreenModel
import com.hybrid.internet.presentation.features.support.SupportContent
import com.hybrid.internet.presentation.features.support.SupportScreenModel

class SupportScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SupportScreenModel>()
        val plansModel = getScreenModel<CustomerPlansScreenModel>()

        val navigator = LocalNavigator.currentOrThrow
        val rootNavigator = navigator.parent ?: navigator
        val planState by plansModel.state.collectAsState()
        val submitState by screenModel.state.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }

        // --- Handle Submission Side Effects ---
        LaunchedEffect(submitState) {
            when (submitState) {
                is UiState.Success -> {
                    // Navigate to Home or Pop the screen on success
//                    navigator.pop()
                    // Or navigator.push(HomeScreen())
                    rootNavigator.push(HomeScreen())
                }
                is UiState.Error -> {
                    val errorMessage = (submitState as UiState.Error).message
                    snackbarHostState.showSnackbar(
                        message = errorMessage ?: "Failed to submit report",
                        duration = SnackbarDuration.Short
                    )
                }
                else -> Unit
            }
        }

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                when (planState) {
                    is UiState.Loading -> {
//                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                            CircularProgressIndicator()
//                        }
                    }
                    is UiState.Success -> {
                        val planData = (planState as UiState.Success<List<PlanResponse>>).data
                        SupportContent(
                            locationResp = planData,
                            onSubmit = { payload ->
                                screenModel.submitReport(
                                    req = SupportRequest(
                                        altMobile = payload["alt_mobile"]?.toString() ?: "",
                                        altEmail = payload["email"]?.toString() ?: "",
                                        category = payload["category"]?.toString() ?: "",
                                        subCategory = payload["sub_category"]?.toString() ?: "",
                                        location = payload["location"]?.toString() ?: "",
                                        remark = payload["remark"]?.toString() ?: "",
                                        message = payload["message"]?.toString() ?: "",
                                        image = payload["image"]?.toString() ?: ""
                                    )
                                )
                            }
                        )
                    }
                    is UiState.Error -> {
                        Text("Error: ${(planState as UiState.Error).message}")
                    }
                    else -> Text("No data found")
                }

                // Show a full-screen loader while submitting
//                if (submitState is UiState.Loading) {
//                    Surface(
//                        modifier = Modifier.fillMaxSize(),
//                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
//                    ) {
//                        Box(contentAlignment = Alignment.Center) {
//                            CircularProgressIndicator()
//                        }
//                    }
//                }
            }
        }
    }
}