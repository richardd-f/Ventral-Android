package com.felix.ventral_android.ui.screens.auth.register.components

import android.net.Uri
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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

private val DarkPurple = Color(0xFF120C1F)
private val AccentPurple = Color(0xFF5D3FD3)
private val LightPurple = Color(0xFFBCAAA4)
private val PureWhite = Color(0xFFFFFFFF)

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
        SimpleInput(bio, "Bio (Optional)", Icons.Default.Info, onBioChange, singleLine = false)
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
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    showDatePicker = false
                }) { Text("OK", color = AccentPurple) }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // --- Profile Picture Section ---
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(PureWhite.copy(alpha = 0.1f))
                .border(2.dp, PureWhite, CircleShape)
                .clickable { /* Trigger Image Picker Logic here */ },
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
                Icon(Icons.Default.CameraAlt, "Upload", tint = PureWhite, modifier = Modifier.size(40.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Upload Profile Picture", color = LightPurple, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(32.dp))

        // --- Date of Birth Section (FIXED) ---
        // We wrap the TextField and the invisible click overlay in a BOX
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = birthDateDisplay,
                onValueChange = { },
                label = { Text("Date of Birth", color = LightPurple) },
                leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = PureWhite) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(), // Fills the parent Box
                shape = RoundedCornerShape(16.dp),
                colors = inputColors()
            )

            // Now matchParentSize works because the parent is the Box above
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
            label = { Text("Password", color = LightPurple) },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = PureWhite) },
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        null, tint = LightPurple
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
            label = { Text("Confirm Password", color = LightPurple) },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = PureWhite) },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = inputColors(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
    }
}