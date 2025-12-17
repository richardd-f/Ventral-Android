package com.felix.ventral_android.ui.screens.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private val DarkPurple = Color(0xFF120C1F)
private val AccentPurple = Color(0xFF5D3FD3)
private val LightPurple = Color(0xFFBCAAA4)
private val PureWhite = Color(0xFFFFFFFF)

@Composable
fun EditProfilePage(
    navController: NavController,
    viewModel: EditProfileViewModel
) {
    val state by viewModel.uiState.collectAsState()

    EditProfileContent(
        state = state,
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onSaveClick = viewModel::onSaveProfile,
        onVerifyClick = viewModel::onVerifyAccount
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    state: EditProfileUiState,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onVerifyClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkPurple, Color(0xFF1F1235))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center
        ) {

            // Header
            Text(
                text = "Edit Profile",
                color = PureWhite,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Update your personal information",
                color = LightPurple,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Verify Account Button (NEW)
            OutlinedButton(
                onClick = onVerifyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = Brush.horizontalGradient(
                        listOf(PureWhite, AccentPurple)
                    )
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = PureWhite
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (state.isVerified) "Account Verified" else "Verify Account",
                    color = PureWhite,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Name
            OutlinedTextField(
                value = state.name,
                onValueChange = onNameChange,
                label = { Text("Full Name", color = LightPurple) },
                leadingIcon = { Icon(Icons.Default.Person, null, tint = PureWhite) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = profileTextFieldColors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = { Text("Email", color = LightPurple) },
                leadingIcon = { Icon(Icons.Default.Email, null, tint = PureWhite) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = profileTextFieldColors()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone
            OutlinedTextField(
                value = state.phone,
                onValueChange = onPhoneChange,
                label = { Text("Phone Number", color = LightPurple) },
                leadingIcon = { Icon(Icons.Default.Phone, null, tint = PureWhite) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = profileTextFieldColors()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Error
            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Save Button
            Button(
                onClick = onSaveClick,
                enabled = !state.isLoading,
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
                        text = "Save Changes",
                        color = DarkPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun profileTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = PureWhite,
        unfocusedBorderColor = AccentPurple.copy(alpha = 0.5f),
        focusedTextColor = PureWhite,
        unfocusedTextColor = PureWhite,
        cursorColor = PureWhite,
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent
    )
