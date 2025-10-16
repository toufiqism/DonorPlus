package com.tofiq.blood

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tofiq.blood.auth.ui.LoginScreen
import com.tofiq.blood.auth.ui.RegisterScreen
import com.tofiq.blood.ui.theme.DonorPlusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DonorPlusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DonorPlusApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DonorPlusApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onRegisterClick = { navController.navigate("register") },
                onLoggedIn = { /* TODO: navigate to home when available */ }
            )
        }
        composable("register") {
            RegisterScreen(
                onLoginClick = { navController.popBackStack() },
                onRegistered = { navController.popBackStack() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    DonorPlusTheme {
        DonorPlusApp()
    }
}