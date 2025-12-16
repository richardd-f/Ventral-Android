package com.felix.ventral_android.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.felix.ventral_android.ui.screens.auth.login.LoginPage
import com.felix.ventral_android.ui.screens.auth.login.LoginViewModel
import com.felix.ventral_android.ui.screens.auth.register.RegisterPage
import com.felix.ventral_android.ui.screens.auth.register.RegisterViewModel
import com.felix.ventral_android.ui.screens.createEvent.CreateEventPage
import com.felix.ventral_android.ui.screens.createEvent.CreateEventViewModel
import com.felix.ventral_android.ui.screens.homepage.HomePage
import com.felix.ventral_android.ui.screens.homepage.HomepageViewModel
import com.felix.ventral_android.ui.screens.profile.ProfilePage
import com.felix.ventral_android.ui.screens.profile.ProfileViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object CreateEvent : Screen("createEvent")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // Login
        composable(Screen.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginPage(navController, viewModel)
        }

        // Register
        composable(Screen.Register.route) {
            val viewModel: RegisterViewModel = hiltViewModel()
            RegisterPage(navController, viewModel)
        }

        // Home
        composable(Screen.Home.route) {
            val viewModel: HomepageViewModel = hiltViewModel()
            HomePage(navController, viewModel)
        }

        // Profile
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfilePage(navController, viewModel)
        }

        // Create Event Page
        composable(Screen.CreateEvent.route) {
            val viewModel: CreateEventViewModel = hiltViewModel()
            CreateEventPage(navController, viewModel)
        }


    }
}