package com.felix.ventral_android.ui.screens.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.navigation.Screen

// THEME: Define the color palette provided
private val DarkPurple = Color(0xFF120C1F)
private val AccentPurple = Color(0xFF5D3FD3)
private val LightPurple = Color(0xFFBCAAA4)
private val PureWhite = Color(0xFFFFFFFF)

@Composable
fun HomePage(
    navController: NavController,
    viewModel: HomepageViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    HomepageContent(navController, state)
}

@Composable
fun HomepageContent(
    navController: NavController,
    state: HomepageUiState
) {
    // THEME: Set the background of the entire screen to DarkPurple
    Box(modifier = Modifier
        .fillMaxSize()
        .background(DarkPurple)
    ) {
        if (state.isLoading) {
            // THEME: Make the loading indicator stand out against the dark background
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AccentPurple
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Available Events",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        // THEME: Use PureWhite for the main title
                        color = PureWhite
                    )
                }

                // --- FIX: Developer Navigation moved here ---
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Developer Navigation:",
                            // THEME: Use LightPurple for secondary text
                            color = LightPurple
                        )
                        // THEME: Apply colors to all developer buttons
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { navController.navigate(Screen.Profile.route) },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                            ) {
                                Text("Profile", color = PureWhite)
                            }
                            Button(
                                onClick = { navController.navigate(Screen.Login.route) },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                            ) {
                                Text("Login", color = PureWhite)
                            }
                            Button(
                                onClick = { navController.navigate(Screen.CreateEvent.route) },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                            ) {
                                Text("Create", color = PureWhite)
                            }
                        }
                    }
                }

                if (state.error != null) {
                    item {
                        Text(
                            text = state.error,
                            // Use standard error color for visibility
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }

                items(state.events) { event ->
                    EventCard(
                        event = event,
                        onApplyClicked = { /* TODO: Handle Apply Click */ }
                    )
                }
                // --- Developer Navigation was removed from the bottom ---
            }
        }
    }
}

@Composable
fun EventCard(event: Event, onApplyClicked: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        // THEME: Set the card background to a slightly lighter shade than the main background
        colors = CardDefaults.cardColors(containerColor = DarkPurple.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(model = event.images.firstOrNull()),
                contentDescription = "Event Image for ${event.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = event.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    // THEME: Use PureWhite for the event title
                    color = PureWhite
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyMedium,
                    // THEME: Use LightPurple for the event description
                    color = LightPurple
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        IconWithText(icon = Icons.Default.ThumbUp, text = event.likes.toString())
                        IconWithText(icon = Icons.Default.ThumbDown, text = "0")
                        IconWithText(icon = Icons.Default.Comment, text = "")
                    }
                    Button(
                        onClick = onApplyClicked,
                        // THEME: Use AccentPurple for the main action button on the card
                        colors = ButtonDefaults.buttonColors(containerColor = AccentPurple)
                    ) {
                        Text("Apply", color = PureWhite)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Event details:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    // THEME: Use PureWhite for subheadings
                    color = PureWhite
                )
                Spacer(modifier = Modifier.height(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconWithText(icon = Icons.Default.LocationOn, text = "Online / To be confirmed")
                    IconWithText(icon = Icons.Default.CalendarToday, text = event.dateStart)
                    IconWithText(icon = Icons.Outlined.AttachMoney, text = event.price.toString())
                }
            }
        }
    }
}

@Composable
fun IconWithText(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            // THEME: Use LightPurple for icons
            tint = LightPurple
        )
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            // THEME: Use LightPurple for icon text
            color = LightPurple
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomepagePreview() {
    val previewState = HomepageUiState(
        isLoading = false,
        events = listOf(
            // FIX: Replaced "Cantus Euforia" with a generic placeholder name for the preview
            Event(
                id = "1", authorId = "a1", name = "Sample Event Title", description = "This is a sample description for the preview event.",
                dateStart = "2026-01-17", dateEnd = "2026-01-18", price = 50000, status = "active", quota = 100,
                images = emptyList(), categories = emptyList(), likes = 123
            )
        )
    )
    HomepageContent(navController = rememberNavController(), state = previewState)
}
