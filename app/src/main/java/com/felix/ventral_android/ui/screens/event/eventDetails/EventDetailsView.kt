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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.ThumbDownOffAlt
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.font.FontWeight
import com.felix.ventral_android.navigation.Screen

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
    val isRegistering by viewModel.isRegistering.collectAsStateWithLifecycle()
    val isAuthor by viewModel.isAuthor.collectAsStateWithLifecycle()

    val onRegisterSuccess: () -> Unit = {
        navController.popBackStack()
    }
    val onDeleteSuccess: () -> Unit = {
        navController.popBackStack()
    }

    event?.let { eventData ->
        EventDetailsContent(
            event = eventData,
            isLiked = isLiked,
            isRegistering = isRegistering,
            isAuthor = isAuthor,
            onBackClick = { navController.popBackStack() },
            onEditClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("event", eventData)
                navController.navigate(Screen.CreateEvent.route)
            },
            onToggleLike = { viewModel.toggleLike() },
            onRegisterClick = { viewModel.registerEvent(onRegisterSuccess) },
            onDeleteClick = {viewModel.deleteEvent (onDeleteSuccess) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun EventDetailsContent(
    event: Event,
    isLiked: Boolean,
    isRegistering: Boolean,
    isAuthor: Boolean,
    onEditClick: () -> Unit = {},
    onDeleteClick: ()-> Unit ={},
    isDisliked: Boolean = false,
    onBackClick: () -> Unit,
    onToggleLike: () -> Unit,
    onToggleDislike: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    val pagerState = rememberPagerState(pageCount = { event.images.size })
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .navigationBarsPadding()
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
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onRegisterClick,
                        enabled = !isRegistering && event.status == "OPEN",
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(50.dp).widthIn(min = 140.dp)
                    ) {
                        if (isRegistering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = if (event.status == "OPEN") "Register Event" else "Closed",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
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

                // Top Controls Overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 12.dp, end = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Surface(shape = CircleShape, color = Color.Black.copy(alpha = 0.4f)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.White, modifier = Modifier.padding(8.dp))
                        }
                    }

                    if (isAuthor) {
                        IconButton(onClick = onEditClick) {
                            Surface(shape = CircleShape, color = Color.Black.copy(alpha = 0.4f)) {
                                Icon(Icons.Default.Edit, "Edit", tint = Color.White, modifier = Modifier.padding(8.dp))
                            }
                        }

                        IconButton(onClick = onDeleteClick) {
                            Surface(shape = CircleShape, color = Color.Black.copy(alpha = 0.4f)) {
                                Icon(Icons.Default.Delete, "Delete", tint = Color.White, modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
            } // End of Box

            // --- 2. Main Content Section (Correctly outside the Image Box) ---
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = event.name,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.SemiBold
                    )
                    StatusLabel(event.status)
                }

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    event.categories.forEach { cat ->
                        SuggestionChip(
                            onClick = { },
                            label = { Text(cat.category, color = Color.White) },
                            shape = RoundedCornerShape(8.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            border = null
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
                        InteractionButton(
                            icon = if (isLiked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            label = "${event.likes + if (isLiked) 1 else 0}",
                            color = if (isLiked) Color.Red else MaterialTheme.colorScheme.onSurface,
                            onClick = onToggleLike
                        )
                        InteractionButton(
                            icon = if (isDisliked) Icons.Filled.ThumbDown else Icons.Outlined.ThumbDownOffAlt,
                            label = "Dislike",
                            onClick = onToggleDislike
                        )
                        InteractionButton(
                            icon = Icons.Outlined.ChatBubbleOutline,
                            label = "Comment",
                            onClick = onCommentClick
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                InfoSection(Icons.Default.DateRange, "Event Date", "${event.dateStart} - ${event.dateEnd}")

                if (event.quota != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoSection(Icons.Default.Person, "Availability", "${event.quota} Slots Available")
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("About Event", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
fun InteractionButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, color: Color = MaterialTheme.colorScheme.onSurface, onClick: () -> Unit) {
    TextButton (onClick = onClick) {
        Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(6.dp))
        Text(label, style = MaterialTheme.typography.labelLarge, color = color)
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