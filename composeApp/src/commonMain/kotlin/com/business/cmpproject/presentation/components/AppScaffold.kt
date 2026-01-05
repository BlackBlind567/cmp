package com.business.cmpproject.presentation.components

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.business.cmpproject.core.state.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun AppScaffold(
    events: Flow<UiEvent>,
    content: @Composable () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        events.collect { event ->
            if (event is UiEvent.ShowSnackBar) {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {
        content()
    }
}