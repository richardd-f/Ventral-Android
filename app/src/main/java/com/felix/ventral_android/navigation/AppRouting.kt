package com.felix.ventral_android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.felix.ventral_android.ui.screens.homepage.Homepage
import com.felix.ventral_android.ui.screens.homepage.HomepageViewModel
import com.felix.ventral_android.ui.screens.profile.Profile
import com.felix.ventral_android.ui.screens.profile.ProfileViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Home
        composable(Screen.Home.route) {
            val viewModel: HomepageViewModel = hiltViewModel()
            Homepage(navController, viewModel)
        }

        // Profile
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            Profile(navController, viewModel)
        }
    }
}