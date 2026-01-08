package com.felix.ventral_android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.felix.ventral_android.ui.components.BottomNavigationBar
import com.felix.ventral_android.ui.screens.auth.login.LoginPage
import com.felix.ventral_android.ui.screens.auth.login.LoginViewModel
import com.felix.ventral_android.ui.screens.auth.register.RegisterPage
import com.felix.ventral_android.ui.screens.auth.register.RegisterViewModel
import com.felix.ventral_android.ui.screens.event.createUpdateEvent.CreateEventPage
import com.felix.ventral_android.ui.screens.event.createUpdateEvent.CreateEventViewModel
import com.felix.ventral_android.ui.screens.event.eventDetails.EventDetailsPage
import com.felix.ventral_android.ui.screens.event.eventDetails.EventDetailsViewModel
import com.felix.ventral_android.ui.screens.homepage.HomePage
import com.felix.ventral_android.ui.screens.homepage.HomepageViewModel
import com.felix.ventral_android.ui.screens.profile.ProfilePage
import com.felix.ventral_android.ui.screens.profile.ProfileViewModel
import com.felix.ventral_android.ui.screens.search.SearchPage
import com.felix.ventral_android.ui.screens.search.SearchViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Login : Screen("login")
    object Register : Screen("register")
    object CreateEvent : Screen("createEvent")
    object EventDetails : Screen("eventDetails")
    object Search : Screen("search")

    companion object {
        // Set screen that has bottom bar
        val bottomBarRoutes = setOf(
            Home.route,
            Profile.route,
            Search.route
        )
    }
}

    @Composable
    fun AppNavigation() {
        val navController = rememberNavController()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Scaffold(
            bottomBar = {
                if (currentRoute in Screen.bottomBarRoutes) {
                    BottomNavigationBar(navController = navController)
                }
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
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

                // Search
                composable(Screen.Search.route) {
                    val viewModel: SearchViewModel = hiltViewModel()
                    SearchPage(navController, viewModel)
                }

                // Create Event (NO bottom bar)
                composable(Screen.CreateEvent.route) {
                    val viewModel: CreateEventViewModel = hiltViewModel()
                    CreateEventPage(navController, viewModel)
                }

                // Event Details (NO bottom bar)
                composable(Screen.EventDetails.route) {
                    val viewModel: EventDetailsViewModel = hiltViewModel()
                    EventDetailsPage(navController, viewModel)
                }
            }
        }
    }