package com.felix.ventral_android.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.navigation.Screen
import com.felix.ventral_android.ui.screens.homepage.components.HomeEventCard

// --- FIX: Define the color palette for theming ---
private val DarkPurple = Color(0xFF120C1F)
private val LightPurple = Color(0xFFBCAAA4)
private val PureWhite = Color(0xFFFFFFFF)

@Composable
fun SearchPage(
    navController: NavController,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // --- FIX: Create a FocusRequester to control focus ---
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        containerColor = DarkPurple, // Set the background for the whole screen
        topBar = {
            SearchTopBar(
                query = state.searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged,
                // Pass the focusRequester to the TopBar
                focusRequester = focusRequester
            )
        }
    ) { padding ->
        SearchContent(
            modifier = Modifier.padding(padding),
            state = state,
            onEventClick = { event ->
                // Navigate to details page, passing the event object
                navController.currentBackStackEntry?.savedStateHandle?.set("event", event)
                navController.navigate(Screen.EventDetails.route)
            },
            onRetry = viewModel::onRetry
        )
    }

    // --- FIX: Request focus when the screen is first composed ---
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester // Receive the focusRequester
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        // --- FIX: Apply the focusRequester to the TextField ---
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        placeholder = { Text("Search events by title...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                }
            }
        },
        singleLine = true,
        // --- FIX: Apply the custom theme colors ---
        colors = TextFieldDefaults.colors(
            focusedContainerColor = DarkPurple,
            unfocusedContainerColor = DarkPurple,
            focusedTextColor = PureWhite,
            unfocusedTextColor = PureWhite,
            cursorColor = PureWhite,
            focusedPlaceholderColor = LightPurple,
            unfocusedPlaceholderColor = LightPurple,
            focusedLeadingIconColor = LightPurple,
            unfocusedLeadingIconColor = LightPurple,
            focusedTrailingIconColor = LightPurple,
            unfocusedTrailingIconColor = LightPurple,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    state: SearchUiState,
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
            !state.isLoading && state.events.isEmpty() && state.searchQuery.isNotEmpty() -> {
                EmptyState(
                    message = "No events found for \"${state.searchQuery}\"",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            !state.isLoading && state.events.isEmpty() && state.searchQuery.isBlank() -> {
                EmptyState(
                    message = "No events available to search.",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
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
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- FIX: Use a theme-appropriate color ---
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry, modifier = Modifier.padding(top = 8.dp)) { Text("Retry") }
    }
}

@Composable
fun EmptyState(message: String, modifier: Modifier = Modifier) {
    // --- FIX: Use a theme-appropriate color ---
    Text(message, color = LightPurple, modifier = modifier)
}
