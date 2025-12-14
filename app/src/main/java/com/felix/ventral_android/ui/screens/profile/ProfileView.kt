package com.felix.ventral_android.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felix.ventral_android.navigation.Screen
import okhttp3.Route


@Composable
fun ProfilePage(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
){
    ProfileContent(navController)
}

@Composable
fun ProfileContent(
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Halo Ini Adalah Profile View")
        Button(
            onClick = { navController.navigate(Screen.Home.route) }
        ) {
            Text("Kembali ke Home")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfilePreview(){
    ProfileContent(navController = rememberNavController())
}