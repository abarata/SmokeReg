package com.smokereg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.smokereg.navigation.NavigationGraph
import com.smokereg.ui.theme.SmokeRegTheme

/**
 * Main activity for the SmokeReg application
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the app edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val app = application as SmokeRegApplication

            SmokeRegTheme {
                SetStatusBarColor()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationGraph(
                        smokeViewModelFactory = app.smokeViewModelFactory,
                        dashboardViewModelFactory = app.dashboardViewModelFactory
                    )
                }
            }
        }
    }
}

/**
 * Sets the status bar color to match the app theme
 */
@Composable
private fun SetStatusBarColor() {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = MaterialTheme.colorScheme.primary

    LaunchedEffect(statusBarColor) {
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = false
        )
    }
}