package com.smokereg.navigation

/**
 * Sealed class representing app navigation routes
 */
sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Dashboard : Screen("dashboard")
}