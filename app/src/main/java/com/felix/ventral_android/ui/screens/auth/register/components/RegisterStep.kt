package com.felix.ventral_android.ui.screens.auth.register.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxScopeInstance.matchParentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.felix.ventral_android.ui.components.SimpleInput
import com.felix.ventral_android.ui.components.inputColors

// --- STEP 1: Personal Info ---
@Composable
fun Step1Content(
    name: String,
    email: String,
    phone: String,
    bio: String,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBioChange: (String) -> Unit
) {
    Column {
        SimpleInput(name, "Full Name", Icons.Default.Person, onNameChange)
        Spacer(modifier = Modifier.height(16.dp))
        SimpleInput(email, "Email", Icons.Default.Email, onEmailChange, KeyboardType.Email)
        Spacer(modifier = Modifier.height(16.dp))
        SimpleInput(phone, "Phone Number", Icons.Default.Phone, onPhoneChange, KeyboardType.Phone)
        Spacer(modifier = Modifier.height(16.dp))
        SimpleInput(bio, "Bio", Icons.Default.Info, onBioChange, singleLine = false)
    }
}

// --- STEP 2: Profile & Date ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step2Content(
    profileImageUri: Uri?,
    birthDateDisplay: String,
    onImageSelected: (Uri?) -> Unit,
    onDateSelected: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }

    // Pick Image Logic
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            onImageSelected(uri)
        }
    )

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) { Text("OK", color = MaterialTheme.colorScheme.primary) }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    headlineContentColor = MaterialTheme.colorScheme.onBackground,
                    weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // --- Profile Picture Section ---
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                .clickable {
                    // Pick IMAGE only
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Profile",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.CameraAlt, "Upload", tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(40.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Upload Profile Picture", color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(32.dp))

        // --- Date of Birth Section ---
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = birthDateDisplay,
                onValueChange = { },
                label = { Text("Date of Birth", color = MaterialTheme.colorScheme.onSurface) },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = MaterialTheme.colorScheme.onBackground) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(), // Fills the parent Box
                shape = RoundedCornerShape(16.dp),
                colors = inputColors()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePicker = true }
            )
        }
    }
}

// --- STEP 3: Security ---
@Composable
fun Step3Content(
    password: String,
    confirmPassword: String,
    isPasswordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password", color = MaterialTheme.colorScheme.onSurface) },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.onBackground) },
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        null, tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = inputColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = { Text("Confirm Password", color = MaterialTheme.colorScheme.onSurface) },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = MaterialTheme.colorScheme.onBackground) },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = inputColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}