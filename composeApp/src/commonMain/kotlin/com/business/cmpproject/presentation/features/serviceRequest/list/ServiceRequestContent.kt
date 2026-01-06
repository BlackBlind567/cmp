package com.business.cmpproject.presentation.features.serviceRequest.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.business.cmpproject.core.state.UiState
import com.business.cmpproject.data.model.response.ServiceRequestItem
import com.business.cmpproject.presentation.theme.CreamBackground
import com.business.cmpproject.presentation.theme.DarkBackground
import com.business.cmpproject.presentation.theme.GreenPrimary
import com.business.cmpproject.presentation.theme.PinkPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceRequestContent(
    uiState: UiState<List<ServiceRequestItem>>,
    isDark: Boolean,
    canLoadMore: Boolean,
    onBack: () -> Unit,
    onLoadMore: () -> Unit,
    onNewRequest: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val fabColor = if (isDark) PinkPrimary else GreenPrimary

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = if (isDark) DarkBackground else CreamBackground,
        floatingActionButton = {
            // Premium Extended FAB: Shrinks on scroll
            ExtendedFloatingActionButton(
                onClick = onNewRequest,
                containerColor = fabColor,
                contentColor = Color.White,
                expanded = !scrollState.isScrollInProgress,
                icon = { Icon(Icons.Default.Add, "Add Request") },
                text = { Text("New Request", fontWeight = FontWeight.Bold) },
                shape = RoundedCornerShape(16.dp)
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            when (uiState) {
                is UiState.Loading -> { /* Show Premium Shimmer */ }
                is UiState.Success -> {
                    LazyColumn(
                        state = scrollState,
                        contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
                    ) {
                        itemsIndexed(uiState.data) { index, item ->
                            ServiceRequestCard(item, isDark)

                            // Load More Logic
                            if (index == uiState.data.size - 1 && canLoadMore) {
                                LaunchedEffect(Unit) { onLoadMore() }
                            }
                        }

                        if (canLoadMore) {
                            item {
                                Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(strokeWidth = 2.dp, color = fabColor)
                                }
                            }
                        }
                    }
                }
                is UiState.Error -> { /* Show Error View */ }
               else -> Unit
            }
        }
    }
}