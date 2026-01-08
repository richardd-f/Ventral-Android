package com.felix.ventral_android.ui.screens.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.navigation.Screen
import com.felix.ventral_android.ui.screens.homepage.components.HomeEventCard


@Composable
fun HomePage(
    navController: NavController,
    viewModel: HomepageViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchAllEvents()
    }

    Scaffold (
        topBar = {
            HomeTopBar()
        }
    ) { padding ->
        HomeContent(
            modifier = Modifier.padding(padding),
            state = state,
            onEventClick = { event ->
                navController.currentBackStackEntry?.savedStateHandle?.set("event", event)
                navController.navigate(Screen.EventDetails.route)
            },
            onRetry = { viewModel.fetchAllEvents() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar() {
    androidx.compose.material3.TopAppBar(
        title = {
            Text(
                "Ventral",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeUiState,
    onEventClick: (Event) -> Unit,
    onRetry: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            state.errorMessage != null -> {
                ErrorState(message = state.errorMessage, onRetry = onRetry)
            }
            state.events.isEmpty() -> {
                EmptyState(modifier = Modifier.align(Alignment.Center))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    items(state.events, key = { it.id ?: it.name }) { event ->
                        HomeEventCard(
                            event = event,
                            onClick = { onEventClick(event) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry) { Text("Retry") }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Text("No events available at the moment.", modifier = modifier)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomepagePreview(){
//    HomepageContent(navController = rememberNavController())
}