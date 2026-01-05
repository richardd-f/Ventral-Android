package com.felix.ventral_android.ui.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.felix.ventral_android.ui.screens.profile.ProfileUiState

@Composable
fun ProfileHeader(state: ProfileUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Profile Image
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("F", color = MaterialTheme.colorScheme.onPrimary, fontSize = 40.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. User Name
        Text(
            text = state.username,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 3-5. Stats Row (Following, Followers, Events)
        Row(
            modifier = Modifier.fillMaxWidth(0.85f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(count = state.followingCount, label = "Following")
            StatItem(count = state.followersCount, label = "Followers")
            StatItem(count = state.eventsCount, label = "Events")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 6. Bio
        Text(
            text = state.bio,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun StatItem(count: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = count.toString(), color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = label, color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp)
    }
}
