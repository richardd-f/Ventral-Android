package com.felix.ventral_android.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

private val DarkPurple = Color(0xFF120C1F)
private val AccentPurple = Color(0xFF5D3FD3)
private val LightPurple = Color(0xFFBCAAA4)
private val PureWhite = Color(0xFFFFFFFF)


@Composable
fun SimpleInput(
    value: String,
    label: String,
    icon: ImageVector? =null,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    // --- NEW PARAMETERS (with defaults to keep current dimensions/logic) ---
    modifier: Modifier = Modifier,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    readOnly: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    // Logic to handle clicks for ReadOnly fields (like Date/Time pickers)
    val interactionSource = remember { MutableInteractionSource() }

    if (onClick != null) {
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
                if (interaction is PressInteraction.Release) {
                    onClick()
                }
            }
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = LightPurple) },
        leadingIcon = if (icon != null) {
            {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PureWhite
                )
            }
        } else null,
        singleLine = singleLine,
        maxLines = maxLines, // Added support for description box
        readOnly = readOnly, // Added support for non-typable fields
        interactionSource = interactionSource,
        // Merge the passed modifier with fillMaxWidth so it stays consistent
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = inputColors(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            // If it's multiline, usually we want "Default" (Enter = new line), else "Next"
            imeAction = if (singleLine) ImeAction.Next else ImeAction.Default
        )
    )
}

@Composable
fun inputColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = PureWhite,
    unfocusedBorderColor = AccentPurple.copy(alpha = 0.5f),
    focusedTextColor = PureWhite,
    unfocusedTextColor = PureWhite,
    cursorColor = PureWhite,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent
)