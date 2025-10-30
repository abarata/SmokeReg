package com.smokereg.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smokereg.ui.components.BottomNavBar
import com.smokereg.ui.screens.DashboardScreen
import com.smokereg.ui.screens.MainScreen
import com.smokereg.viewmodel.DashboardViewModelFactory
import com.smokereg.viewmodel.SmokeViewModelFactory

/**
 * Main navigation graph for the app
 */
@Composable
fun NavigationGraph(
    smokeViewModelFactory: SmokeViewModelFactory,
    dashboardViewModelFactory: DashboardViewModelFactory,
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.Main.route
            ) {
                composable(route = Screen.Main.route) {
                    MainScreen(viewModelFactory = smokeViewModelFactory)
                }

                composable(route = Screen.Dashboard.route) {
                    DashboardScreen(viewModelFactory = dashboardViewModelFactory)
                }
            }
        }
    }
}