package com.waikikigo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.waikikigo.app.data.WaikikiDataRepository
import com.waikikigo.app.ui.navigation.Screen
import com.waikikigo.app.ui.navigation.bottomNavItems
import com.waikikigo.app.ui.screens.*
import com.waikikigo.app.ui.theme.WaikikiGoTheme
import com.waikikigo.app.ui.theme.WaikikiOceanBlue

class MainActivity : ComponentActivity() {

    private val repository = WaikikiDataRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WaikikiGoTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in bottomNavItems.map { it.route }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomBar) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surface,
                                tonalElevation = 8.dp
                            ) {
                                bottomNavItems.forEach { screen ->
                                    val isSelected = currentRoute == screen.route
                                    NavigationBarItem(
                                        icon = {
                                            screen.icon?.let { icon ->
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = screen.title
                                                )
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = screen.title,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        selected = isSelected,
                                        onClick = {
                                            if (currentRoute != screen.route) {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = WaikikiOceanBlue,
                                            selectedTextColor = WaikikiOceanBlue,
                                            indicatorColor = WaikikiOceanBlue.copy(alpha = 0.15f)
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Onboarding.route,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(Screen.Onboarding.route) {
                            OnboardingScreen(
                                repository = repository,
                                onCompleteOnboarding = {
                                    navController.navigate(Screen.Home.route) {
                                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(Screen.Home.route) {
                            HomeScreen(
                                repository = repository,
                                onSelectRestaurant = { restaurantId ->
                                    navController.navigate(Screen.RestaurantDetail.createRoute(restaurantId))
                                }
                            )
                        }

                        composable(Screen.Quests.route) {
                            QuestScreen(repository = repository)
                        }

                        composable(Screen.Rewards.route) {
                            RewardsScreen(repository = repository)
                        }

                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                repository = repository,
                                onEditPreferences = {
                                    navController.navigate(Screen.Onboarding.route)
                                }
                            )
                        }

                        composable(
                            route = Screen.RestaurantDetail.route,
                            arguments = listOf(navArgument("restaurantId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val restaurantId = backStackEntry.arguments?.getString("restaurantId") ?: ""
                            RestaurantDetailScreen(
                                restaurantId = restaurantId,
                                repository = repository,
                                onBack = { navController.popBackStack() },
                                onStartQuest = {
                                    navController.navigate(Screen.Quests.route)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
