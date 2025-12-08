package com.tofiq.blood.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

import com.tofiq.blood.auth.ui.LoginScreen
import com.tofiq.blood.auth.ui.RegisterScreen
import com.tofiq.blood.auth.ui.SettingsScreen
import com.tofiq.blood.dashboard.DashboardScreen
import kotlinx.serialization.Serializable

/**
 * ============================================================================
 * NAVIGATION 3 IMPLEMENTATION
 * ============================================================================
 *
 * Navigation 3 is a new Jetpack navigation library that provides:
 *
 * 1. TYPE-SAFE ROUTES: Routes are defined as data classes/objects with @Serializable
 *    annotation, eliminating string-based route matching and providing compile-time safety.
 *
 * 2. BACK STACK MANAGEMENT: Uses rememberNavBackStack() to manage navigation state.
 *    The back stack is a mutable list of NavKey objects that you can manipulate directly.
 *
 * 3. ENTRY PROVIDER: Maps NavKey types to their corresponding composable content.
 *    Uses entryProvider { } DSL with entry<RouteType> { } blocks.
 *
 * 4. NAV DISPLAY: Renders the current back stack entry. It observes the back stack
 *    and automatically displays the appropriate content.
 *
 * KEY DIFFERENCES FROM NAVIGATION COMPOSE:
 * - No NavController - use back stack directly
 * - No NavHost - use NavDisplay instead
 * - No string routes - use typed NavKey classes
 * - No composable() DSL - use entry<T> { } instead
 *
 * NAVIGATION OPERATIONS:
 * - Navigate forward: backStack.add(RouteObject)
 * - Navigate back: backStack.removeLastOrNull()
 * - Clear and navigate: backStack.clear(); backStack.add(RouteObject)
 * - Replace current: backStack.removeLast(); backStack.add(RouteObject)
 * ============================================================================
 */

// ============================================================================
// ROUTE DEFINITIONS
// ============================================================================
// Each route is a sealed class/object implementing NavKey.
// Using @Serializable enables type-safe argument passing.
// Objects are used for routes without arguments.
// Data classes are used for routes with arguments.

/**
 * Login screen route - entry point of the app
 */
@Serializable
data object LoginRoute : NavKey

/**
 * Registration screen route
 */
@Serializable
data object RegisterRoute : NavKey

/**
 * Settings screen route
 */
@Serializable
data object SettingsRoute : NavKey

/**
 * Dashboard screen route - main app screen after login
 */
@Serializable
data object DashboardRoute : NavKey

// ============================================================================
// NAVIGATION COMPOSABLE
// ============================================================================

/**
 * Main navigation composable using Navigation 3.
 *
 * This function sets up the entire navigation graph for the app using
 * the new Navigation 3 library's declarative approach.
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    // ========================================================================
    // BACK STACK SETUP
    // ========================================================================
    // rememberNavBackStack creates a saveable, mutable list of NavKey objects.
    // The initial route (LoginRoute) is passed as the starting destination.
    // The back stack survives configuration changes and process death.
    val backStack = rememberNavBackStack(LoginRoute)

    // ========================================================================
    // ENTRY PROVIDER
    // ========================================================================
    // entryProvider defines the mapping between NavKey types and their UI.
    // Each entry<T> block specifies what composable to show for that route type.
    // The 'key' parameter in each entry block is the actual route instance,
    // which can contain arguments for data classes.
    val entryProvider = entryProvider<NavKey> {

        // --------------------------------------------------------------------
        // LOGIN SCREEN ENTRY
        // --------------------------------------------------------------------
        // entry<LoginRoute> maps the LoginRoute type to LoginScreen composable.
        // Navigation callbacks modify the back stack directly.
        entry<LoginRoute> {
            LoginScreen(
                // Navigate to register: add RegisterRoute to back stack
                onRegisterClick = {
                    Log.d("AppNavigation", "Navigating to Register")
                    backStack.add(RegisterRoute)
                },
                // Navigate to dashboard after login: clear stack and add dashboard
                // This prevents user from going back to login after successful auth
                onLoggedIn = {
                    Log.d("AppNavigation", "Login successful - navigating to Dashboard")
                    Log.d("AppNavigation", "Current backStack size before: ${backStack.size}")
                    backStack.clear()
                    backStack.add(DashboardRoute)
                    Log.d("AppNavigation", "Current backStack size after: ${backStack.size}")
                },
                // Navigate to settings: add SettingsRoute to back stack
                onSettingsClick = {
                    Log.d("AppNavigation", "Navigating to Settings")
                    backStack.add(SettingsRoute)
                }
            )
        }

        // --------------------------------------------------------------------
        // REGISTER SCREEN ENTRY
        // --------------------------------------------------------------------
        entry<RegisterRoute> {
            RegisterScreen(
                // Go back to login: remove current entry from back stack
                onLoginClick = {
                    backStack.removeLastOrNull()
                },
                // After registration: clear stack and go to login
                // User needs to login with their new credentials
                onRegistered = {
                    backStack.clear()
                    backStack.add(LoginRoute)
                }
            )
        }

        // --------------------------------------------------------------------
        // SETTINGS SCREEN ENTRY
        // --------------------------------------------------------------------
        entry<SettingsRoute> {
            SettingsScreen(
                // Go back: remove current entry from back stack
                onNavigateBack = {
                    backStack.removeLastOrNull()
                }
            )
        }

        // --------------------------------------------------------------------
        // DASHBOARD SCREEN ENTRY
        // --------------------------------------------------------------------
        entry<DashboardRoute> {
            DashboardScreen(
                // Handle logout: clear back stack and navigate to login
                onLogout = {
                    Log.d("AppNavigation", "Logout - navigating to Login")
                    backStack.clear()
                    backStack.add(LoginRoute)
                }
            )
        }
    }

    // ========================================================================
    // NAV DISPLAY
    // ========================================================================
    // NavDisplay is the UI component that renders the current back stack state.
    // It observes the back stack and automatically shows the appropriate entry.
    // 
    // Parameters:
    // - backStack: The navigation back stack to observe
    // - entryProvider: The mapping of routes to composables
    // - modifier: Optional modifier for the display container
    // 
    // NavDisplay handles:
    // - Rendering the topmost entry in the back stack
    // - Transitions between entries (can be customized)
    // - Predictive back gesture support (on supported devices)
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            CustomNavEntryDecorator(),
        )
    )
}

// import androidx.navigation3.runtime.NavEntryDecorator
class CustomNavEntryDecorator<T : Any> : NavEntryDecorator<T>(
    decorate = { entry ->
        Log.d(
            "CustomNavEntryDecorator",
            "entry with ${entry.contentKey} entered composition and was decorated"
        )
        entry.Content()
    },
    onPop = { contentKey -> Log.d("CustomNavEntryDecorator", "entry with $contentKey was popped") }
)