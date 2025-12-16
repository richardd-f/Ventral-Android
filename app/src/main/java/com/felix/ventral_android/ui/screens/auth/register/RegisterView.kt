package com.felix.ventral_android.ui.screens.auth.register

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.felix.ventral_android.navigation.Screen
import com.felix.ventral_android.ui.screens.auth.register.components.Step1Content
import com.felix.ventral_android.ui.screens.auth.register.components.Step2Content
import com.felix.ventral_android.ui.screens.auth.register.components.Step3Content

@Composable
fun RegisterPage(
    navController: NavController,
    viewModel: RegisterViewModel
){
    val state by viewModel.uiState.collectAsState()

    // navigate to Login
    val onNavigateToLogin = {
        navController.navigate(Screen.Login.route)
    }

    // Navigate to Home if success
    val onRegisterSuccess = {
        navController.navigate(Screen.Home.route)
    }

    RegisterContent(
        state = state,
        // Step 1 Events
        onNameChange = viewModel::onNameChange,
        onEmailChange = viewModel::onEmailChange,
        onPhoneChange = viewModel::onPhoneChange,
        onBioChange = viewModel::onBioChange,
        // Step 2 Events
        onImageSelected = viewModel::onProfileImageSelected,
        onDateSelected = viewModel::onDateSelected,
        // Step 3 Events
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
        onTogglePasswordVisibility = viewModel::togglePasswordVisibility,
        // Navigation Events
        onNextStep = viewModel::onNextStep,
        onPreviousStep = viewModel::onPreviousStep,
        onFinalRegister = { viewModel.onFinalRegister(onRegisterSuccess) },
        onNavigateToLogin = onNavigateToLogin
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterContent(
    state: RegisterUiState,
    // Form Callbacks
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onImageSelected: (Uri?) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    // Nav Callbacks
    onNextStep: () -> Unit,
    onPreviousStep: () -> Unit,
    onFinalRegister: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
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
            // --- Progress Header ---
            Text(
                text = "Step ${state.currentStep} of ${state.totalSteps}",
                color = AccentPurple,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = when(state.currentStep) {
                    1 -> "Personal Details"
                    2 -> "Profile Setup"
                    else -> "Security"
                },
                color = PureWhite,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Animated Form Content ---
            AnimatedContent(
                targetState = state.currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { width -> width } + fadeIn() with
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() with
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                }, label = "RegistrationSteps"
            ) { step ->
                when (step) {
                    1 -> Step1Content(
                        name = state.name,
                        email = state.email,
                        phone = state.phone,
                        bio = state.bio,
                        onNameChange = onNameChange,
                        onEmailChange = onEmailChange,
                        onPhoneChange = onPhoneChange,
                        onBioChange = onBioChange
                    )
                    2 -> Step2Content (
                        profileImageUri = state.profileImageUri,
                        birthDateDisplay = state.birthDateDisplay,
                        onImageSelected = onImageSelected,
                        onDateSelected = onDateSelected
                    )
                    3 -> Step3Content(
                        password = state.password,
                        confirmPassword = state.confirmPassword,
                        isPasswordVisible = state.isPasswordVisible,
                        onPasswordChange = onPasswordChange,
                        onConfirmPasswordChange = onConfirmPasswordChange,
                        onTogglePasswordVisibility = onTogglePasswordVisibility
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Error Message ---
            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // --- Navigation Buttons ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // PREVIOUS
                if (state.currentStep > 1) {
                    OutlinedButton(
                        onClick = onPreviousStep,
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PureWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, PureWhite.copy(alpha = 0.5f))
                    ) {
                        Text("Back")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }

                // NEXT / REGISTER
                Button(
                    onClick = {
                        if (state.currentStep < state.totalSteps) onNextStep()
                        else onFinalRegister()
                    },
                    modifier = Modifier
                        .weight(1f)
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
                            text = if (state.currentStep == state.totalSteps) "Sign Up" else "Next",
                            color = DarkPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Link
            if (state.currentStep == 1) {
                val annotatedString = buildAnnotatedString {
                    append("Already have an account? ")
                    withStyle(style = SpanStyle(color = PureWhite, fontWeight = FontWeight.Bold)) {
                        append("Login")
                    }
                }
                Text(
                    text = annotatedString,
                    color = LightPurple,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RegisterPreview(){
//    RegisterContent(navController = rememberNavController())
}

private val DarkPurple = Color(0xFF120C1F)
private val AccentPurple = Color(0xFF5D3FD3)
private val LightPurple = Color(0xFFBCAAA4)
private val PureWhite = Color(0xFFFFFFFF)