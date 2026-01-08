package com.felix.ventral_android.ui.screens.event.createUpdateEvent

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.navigation.Screen
import com.felix.ventral_android.ui.components.DatePickerField
import com.felix.ventral_android.ui.components.SimpleInput
import com.felix.ventral_android.ui.components.TimePickerField
import com.felix.ventral_android.ui.screens.event.createUpdateEvent.components.ImagePreviewItem

@Composable
fun CreateEventPage(
    navController: NavController,
    viewModel: CreateEventViewModel
) {
    // 1. Get event from navigation
    val eventFromNav = remember {
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Event>("event")
    }

    // 2. Initialize ViewModel once
    LaunchedEffect(eventFromNav) {
        eventFromNav?.let { viewModel.setEditMode(eventFromNav) }
    }

    val state by viewModel.uiState.collectAsState()

    val onNavigateBack = { navController.navigate(Screen.Profile.route) }
    val onEventCreated: (Event)->Unit = {event ->
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.set("event", event)

        navController.popBackStack()
    }

    CreateEventContent(
        state = state,
        // Input Callbacks
        onEventNameChange = viewModel::onEventNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onStartDateChange = viewModel::onStartDateChange,
        onStartTimeChange = viewModel::onStartTimeChange,
        onEndDateChange = viewModel::onEndDateChange,
        onEndTimeChange = viewModel::onEndTimeChange,
        onPriceChange = viewModel::onPriceChange,
        onQuotaChange = viewModel::onQuotaChange,
        onImagesSelected = viewModel::onImagesSelected,
        onCategoryToggled = viewModel::onCategoryToggled,
        onAddressChange = viewModel::onAddressChange,
        onCityChange = viewModel::onCityChange,
        onStatusChange = viewModel::onStatusChange,
        // Actions
        onImagePreviewDelete = viewModel::onImagePreviewDelete,
        onCreateEvent = { viewModel.submit(onEventCreated) },
        onNavigateBack = onNavigateBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventContent(
    state: CreateEventUiState,
    // Input Callbacks
    onAddressChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onEventNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onStartDateChange: (String) -> Unit,
    onStartTimeChange: (String) -> Unit,
    onEndDateChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onQuotaChange: (String) -> Unit,
    onImagesSelected: (List<String>) -> Unit,
    onCategoryToggled: (String) -> Unit,
    // Actions
    onImagePreviewDelete: (String)-> Unit,
    onCreateEvent: () -> Unit,
    onNavigateBack: () -> Unit
) {
    var statusExpanded by remember { mutableStateOf(false) }
    val statusOptions = listOf("OPEN", "CLOSED")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkPurple, Color(0xFF1F1235))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Header ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = PureWhite
                    )
                }
                Text(
                    text = if (state.mode == FormMode.CREATE) "Create New Event"
                    else "Edit Event",
                    color = PureWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- 1. Event Name ---
            SimpleInput(
                value = state.eventName,
                onValueChange = onEventNameChange,
                label = "Event Name",
                icon = Icons.Default.Edit,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- 2. Description ---
            SimpleInput(
                value = state.description,
                onValueChange = onDescriptionChange,
                label = "Description",
                singleLine = false,
                maxLines = 4,
                modifier = Modifier.height(120.dp),
                icon = Icons.Default.Info
            )

            Spacer(modifier = Modifier.height(24.dp))

            // LOCATION SECTION
            Spacer(modifier = Modifier.height(24.dp))

            // --- NEW: Location Section ---
            Text(
                text = "Location",
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            SimpleInput(
                value = state.address,
                onValueChange = onAddressChange,
                label = "Full Address",
                icon = Icons.Default.LocationOn,
            )

            Spacer(modifier = Modifier.height(12.dp))

            SimpleInput(
                value = state.city,
                onValueChange = onCityChange,
                label = "City",
                icon = Icons.Default.Place,
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- NEW: Event Status (Dropdown) ---
            Text(
                text = "Event Status",
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox (
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.status.ifEmpty { "Select Status" },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite,
                        focusedBorderColor = AccentPurple,
                        unfocusedBorderColor = PureWhite.copy(alpha = 0.3f),
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                    ),
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Info, contentDescription = null, tint = AccentPurple) }
                )

                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false },
                    modifier = Modifier.background(DarkPurple)
                ) {
                    statusOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = PureWhite) },
                            onClick = {
                                onStatusChange(option)
                                statusExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Images ---
            Text(
                text = "Event Images",
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            val imagePickerLauncher =
                rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetMultipleContents()
                ) { uris: List<Uri> ->
                    onImagesSelected(uris.map { it.toString() })
                }

            Button(
                onClick = { imagePickerLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Upload Images (${state.images.size})")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Image Preview Section
            if (state.images.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(state.images) { uri ->
                        ImagePreviewItem (
                            uri = uri,
                            onDelete = onImagePreviewDelete
                        )
                    }
                }
            } else {
                // Optional: Placeholder if no images
                Text(
                    text = "No images selected",
                    color = PureWhite.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Date & Time ---
            Text(
                text = "Date & Time",
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    DatePickerField(
                        label = "Start Date",
                        value = state.startDate,
                        onDateSelected = onStartDateChange,
                        icon = Icons.Default.DateRange
                    )
                }
                Box(modifier = Modifier.weight(0.7f)) {
                    TimePickerField(
                        label = "Time",
                        value = state.startTime,
                        onTimeSelected = onStartTimeChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    DatePickerField(
                        label = "End Date",
                        value = state.endDate,
                        onDateSelected = onEndDateChange,
                        icon = Icons.Default.DateRange
                    )
                }
                Box(modifier = Modifier.weight(0.7f)) {
                    TimePickerField(
                        label = "Time",
                        value = state.endTime,
                        onTimeSelected = onEndTimeChange
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Categories ---
            Text(
                text = "Categories",
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.availableCategories.forEach { category ->
                    val selected = state.selectedCategoryIds.contains(category.id)

                    FilterChip(
                        selected = selected,
                        onClick = { onCategoryToggled(category.id) },
                        label = { Text(category.category) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentPurple,
                            selectedLabelColor = PureWhite
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Ticket Details ---
            Text(
                text = "Ticket Details",
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    SimpleInput(
                        value = state.price,
                        onValueChange = onPriceChange,
                        label = "Price (Rp)",
                        keyboardType = KeyboardType.Number,
                        icon = Icons.Default.ShoppingCart,
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    SimpleInput(
                        value = state.quota,
                        onValueChange = onQuotaChange,
                        label = "Quota",
                        keyboardType = KeyboardType.Number,
                        icon = Icons.Default.Person
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- Error Message ---
            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontSize = 14.sp
                )
            }

            // --- Submit Button ---
            Button(
                onClick = onCreateEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PureWhite),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = DarkPurple,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if(state.mode == FormMode.CREATE)"Publish Event"
                        else "Update Event",
                        color = DarkPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


private val DarkPurple = Color(0xFF120C1F)
private val AccentPurple = Color(0xFF5D3FD3)
private val LightPurple = Color(0xFFBCAAA4)
private val PureWhite = Color(0xFFFFFFFF)