package com.felix.ventral_android.ui.screens.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.felix.ventral_android.navigation.Screen


@Composable
fun HomePage(
    navController: NavController,
    viewModel: HomepageViewModel = hiltViewModel()
){
    HomepageContent(navController)
}

@Composable
fun HomepageContent(
    navController: NavController
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Halo Ini adalah Homepage")
        Button(
            onClick = { navController.navigate(Screen.Profile.route) }
        ) {
            Text("Pergi ke Profile")
        }
        Button(
            onClick = { navController.navigate(Screen.Login.route) }
        ) {
            Text("Pergi ke Login")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomepagePreview(){
    HomepageContent(navController = rememberNavController())
}