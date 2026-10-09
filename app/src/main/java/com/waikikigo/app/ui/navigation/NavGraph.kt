package com.waikikigo.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Onboarding : Screen("onboarding", "Sign Up")
    object Home : Screen("home", "Waikiki Map", Icons.Default.Map)
    object Quests : Screen("quests", "Quests", Icons.Default.Explore)
    object Rewards : Screen("rewards", "Rewards", Icons.Default.CardGiftcard)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
    object RestaurantDetail : Screen("restaurant/{restaurantId}", "Restaurant Detail") {
        fun createRoute(restaurantId: String) = "restaurant/$restaurantId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Quests,
    Screen.Rewards,
    Screen.Profile
)
