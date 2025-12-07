package com.tofiq.blood

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.messaging.FirebaseMessaging
import com.tofiq.blood.navigation.AppNavigation
import com.tofiq.blood.ui.theme.DonorPlusTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * ============================================================================
 * MAIN ACTIVITY - NAVIGATION 3 IMPLEMENTATION
 * ============================================================================
 * 
 * This app uses Jetpack Navigation 3, the new type-safe navigation library.
 * 
 * KEY BENEFITS OF NAVIGATION 3:
 * 
 * 1. TYPE SAFETY: Routes are defined as Kotlin classes/objects with @Serializable,
 *    providing compile-time safety instead of error-prone string matching.
 * 
 * 2. SIMPLER API: No NavController needed - directly manipulate the back stack
 *    using standard list operations (add, remove, clear).
 * 
 * 3. BETTER COMPOSE INTEGRATION: Designed from the ground up for Compose,
 *    with a more declarative and idiomatic API.
 * 
 * 4. PREDICTIVE BACK SUPPORT: Built-in support for Android 14+ predictive
 *    back gestures without additional configuration.
 * 
 * MIGRATION FROM NAVIGATION COMPOSE:
 * - NavController → rememberNavBackStack()
 * - NavHost → NavDisplay
 * - composable("route") → entry<RouteType>
 * - navController.navigate("route") → backStack.add(RouteObject)
 * - navController.popBackStack() → backStack.removeLastOrNull()
 * 
 * See AppNavigation.kt for the complete navigation setup.
 * ============================================================================
 */
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

/**
 * Root composable for the DonorPlus app.
 * 
 * Sets up Firebase Cloud Messaging token retrieval and delegates
 * navigation to AppNavigation which uses Navigation 3.
 */
@Composable
fun DonorPlusApp() {
    // Firebase Cloud Messaging token retrieval for push notifications
    LaunchedEffect(Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM_TOKEN", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("FCM_TOKEN", "Your token is: $token")
        }
    }
    
    // Navigation 3 handles all app navigation
    // See AppNavigation.kt for route definitions and navigation logic
    AppNavigation()
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    DonorPlusTheme {
        DonorPlusApp()
    }
}