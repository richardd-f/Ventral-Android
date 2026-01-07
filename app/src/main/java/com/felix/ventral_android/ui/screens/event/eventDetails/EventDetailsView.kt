package com.felix.ventral_android.ui.screens.event.eventDetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.felix.ventral_android.domain.model.Event
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ThumbDownOffAlt
import androidx.compose.material.icons.filled.ThumbDown

@Composable
fun EventDetailsPage(
    navController: NavController,
    viewModel: EventDetailsViewModel
) {
    // 1. Get event from navigation
    val eventFromNav = remember {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Event>("event")
    }

    // 2. Initialize ViewModel once
    LaunchedEffect(eventFromNav) {
        eventFromNav?.let { viewModel.setEvent(it) }
    }

    // 3. Collect states
    val event by viewModel.eventState.collectAsStateWithLifecycle()
    val isLiked by viewModel.isLiked.collectAsStateWithLifecycle()

    event?.let { eventData ->
        EventDetailsContent(
            event = eventData,
            isLiked = isLiked,
            onBackClick = { navController.popBackStack() },
            onToggleLike = { viewModel.toggleLike() }
        )
    }
}

// Add these imports


//@OptIn(ExperimentalMaterial3Api::experimentalmaterial3api, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)

@Composable
fun EventDetailsContent(
    event: Event,
    isLiked: Boolean,
    isDisliked: Boolean = false, // Add to your ViewModel/State if needed
    onBackClick: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleDislike: () -> Unit = {}, // Placeholder
    onCommentClick: () -> Unit = {}   // Placeholder
) {
    val pagerState = rememberPagerState(pageCount = { event.images.size })
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            Surface(shadowElevation = 8.dp, color = MaterialTheme.colorScheme.surface) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Price", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        Text(
                            text = if (event.price == 0) "Free" else "Rp ${String.format("%,d", event.price)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                    }
                    androidx.compose.material3.Button(
                        onClick = { /* Handle registration */ },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Text("Register Event", modifier = Modifier.padding(horizontal = 8.dp))
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(bottom = padding.calculateBottomPadding())
                .verticalScroll(scrollState)
                .fillMaxSize()
        ) {
            // --- 1. Header Image Section ---
            Box(modifier = Modifier.height(320.dp)) {
                HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { index ->
                    AsyncImage(
                        model = event.images[index],
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Back Button
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(top = 40.dp, start = 12.dp).align(Alignment.TopStart)
                ) {
                    Surface(shape = androidx.compose.foundation.shape.CircleShape, color = Color.Black.copy(alpha = 0.4f)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White, modifier = Modifier.padding(8.dp))
                    }
                }
            }

            // --- 2. Main Content Section ---
            Column(modifier = Modifier.padding(20.dp)) {

                // Title and Status
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = event.name,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f),
                        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                    )
                    StatusLabel(event.status)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- 3. Wrapped Categories (FlowRow) ---
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp) // Space between wrapped rows
                ) {
                    event.categories.forEach { cat ->
                        androidx.compose.material3.SuggestionChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = cat.category,
                                    color = Color.White // Change text color to white
                                )
                            },
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            shape = RoundedCornerShape(8.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- 4. Social Bar (Like, Dislike, Comment) ---
                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Like
                        InteractionButton(
                            icon = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            label = "${event.likes + if (isLiked) 1 else 0}",
                            color = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurface,
                            onClick = onToggleLike
                        )

                        // Dislike
                        InteractionButton(
                            icon = if (isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDownOffAlt,
                            label = "Dislike",
                            color = if (isDisliked) Color.DarkGray else MaterialTheme.colorScheme.onSurface,
                            onClick = onToggleDislike
                        )

                        // Comment
                        InteractionButton(
                            icon = Icons.Outlined.ChatBubbleOutline,
                            label = "Comment",
                            onClick = onCommentClick
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- 5. Info Grid ---
                InfoSection(
                    icon = androidx.compose.material.icons.Icons.Default.DateRange,
                    title = "Event Date",
                    subtitle = "${event.dateStart} - ${event.dateEnd}"
                )

                if (event.quota != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoSection(
                        icon = androidx.compose.material.icons.Icons.Default.Person,
                        title = "Availability",
                        subtitle = "${event.quota} Slots Available"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("About Event", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun InteractionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    androidx.compose.material3.TextButton(onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.labelLarge, color = color)
        }
    }
}

@Composable
fun InfoSection(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.labelLarge, color = Color.Gray)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun StatusLabel(status: String) {
    val color = when (status) {
        "OPEN" -> Color(0xFF4CAF50)
        "CLOSED" -> Color.Red
        else -> Color.Gray
    }
    Surface (
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color
        )
    }
}