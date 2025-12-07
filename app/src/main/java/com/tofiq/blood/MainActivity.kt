package com.tofiq.blood

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.tofiq.blood.auth.ui.LoginScreen
import com.tofiq.blood.auth.ui.RegisterScreen
import com.tofiq.blood.auth.ui.SettingsScreen
import com.tofiq.blood.dashboard.DashboardScreen
import com.tofiq.blood.ui.theme.DonorPlusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DonorPlusTheme {
                DonorPlusApp()
            }
        }
    }
}

@Composable
fun DonorPlusApp() {
    LaunchedEffect(Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and copy this token
            // This is the "address" you need for the Firebase console
            Log.d("FCM_TOKEN", "Your token is: $token")
        }
    }
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onRegisterClick = { navController.navigate("register") },
                onLoggedIn = { 
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onSettingsClick = { navController.navigate("settings") }
            )
        }
        composable("register") {
            RegisterScreen(
                onLoginClick = { navController.popBackStack() },
                onRegistered = { 
                    // Navigate back to login screen after successful registration
                    // Clear back stack and navigate to login
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("dashboard") {
            DashboardScreen()
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