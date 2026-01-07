package com.business.cmpproject.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.business.cmpproject.presentation.features.splash.SplashScreen
import com.business.cmpproject.presentation.features.support.SupportContent
import com.business.cmpproject.presentation.theme.AppTheme


@Composable
fun App() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            Navigator(SplashScreen())
        }
    }
}
