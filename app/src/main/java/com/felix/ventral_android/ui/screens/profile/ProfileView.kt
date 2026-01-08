package com.felix.ventral_android.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.felix.ventral_android.navigation.Screen
import com.felix.ventral_android.ui.components.EventPostCard
import com.felix.ventral_android.ui.screens.profile.components.ProfileHeader
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState


@Composable
fun ProfilePage(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // always request when load this page
    LaunchedEffect(Unit) {
        viewModel.fetchProfileData()
    }

    // Navigation events (logout / session expired)
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            navController.navigate(screen.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = state.isLoading),
        onRefresh = { viewModel.fetchProfileData() }
    ) {
        ProfileContent(
            navController = navController,
            state = state,
            onRefresh = {
                viewModel.fetchProfileData()
            }
        )
    }
}

@Composable
fun ProfileContent(
    navController: NavController,
    state: ProfileUiState,
    onRefresh: () -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = state.isLoading
    )

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = onRefresh
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(top = 50.dp)
        ) {

            if (state.isLoading && state.posts.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 96.dp)
                ) {
                    item {
                        ProfileHeader(state)
                    }

                    item {
                        Text(
                            text = "My Events",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                horizontal = 24.dp,
                                vertical = 16.dp
                            )
                        )
                    }

                    items(state.posts) { post ->
                        EventPostCard(
                            post = post,
                            navController = navController
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.CreateEvent.route)
                },
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 24.dp,
                        bottom = 88.dp
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Event"
                )
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfilePreview(){
//    ProfileContent(navController = rememberNavController())
}