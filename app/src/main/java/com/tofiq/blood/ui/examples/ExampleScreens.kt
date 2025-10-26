package com.tofiq.blood.ui.examples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tofiq.blood.ui.components.DonorPlusAnimatedCard
import com.tofiq.blood.ui.components.DonorPlusAnimatedLogo
import com.tofiq.blood.ui.components.DonorPlusCard
import com.tofiq.blood.ui.components.DonorPlusContentContainer
import com.tofiq.blood.ui.components.DonorPlusErrorMessage
import com.tofiq.blood.ui.components.DonorPlusGradientBackground
import com.tofiq.blood.ui.components.DonorPlusHeader
import com.tofiq.blood.ui.components.DonorPlusInfoMessage
import com.tofiq.blood.ui.components.DonorPlusLoadingIndicator
import com.tofiq.blood.ui.components.DonorPlusOutlinedButton
import com.tofiq.blood.ui.components.DonorPlusPrimaryButton
import com.tofiq.blood.ui.components.DonorPlusSecondaryButton
import com.tofiq.blood.ui.components.DonorPlusSectionHeader
import com.tofiq.blood.ui.components.DonorPlusSolidBackground
import com.tofiq.blood.ui.components.DonorPlusSpacing
import com.tofiq.blood.ui.components.DonorPlusSuccessMessage
import com.tofiq.blood.ui.components.DonorPlusTextField
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import kotlinx.coroutines.delay

/**
 * Example Screens Using DonorPlus Design System
 * 
 * These examples demonstrate how to use the design system components
 * to build consistent, beautiful screens throughout the app.
 * 
 * Copy these patterns when creating new features!
 */

// ==================== Example 1: Profile Edit Screen ====================

/**
 * Example of a form screen using the design system.
 * Pattern: Solid background + Card + Form fields + Primary button
 */
@Composable
fun ExampleProfileEditScreen() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    
    DonorPlusSolidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(DonorPlusSpacing.L)
        ) {
            // Page Header
            DonorPlusSectionHeader(text = "Edit Profile")
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            // Form Card
            DonorPlusCard {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    // Full Name Field
                    DonorPlusTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Full Name",
                        leadingIcon = Icons.Default.Person
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    
                    // Email Field
                    DonorPlusTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        leadingIcon = Icons.Default.Email
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    
                    // Phone Field
                    DonorPlusTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = "Phone Number",
                        leadingIcon = Icons.Default.Phone
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
                    
                    // Success Message
                    if (showSuccess) {
                        DonorPlusSuccessMessage(
                            message = "Profile updated successfully!"
                        )
                        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    }
                    
                    // Save Button
                    if (isLoading) {
                        DonorPlusLoadingIndicator()
                    } else {
                        DonorPlusPrimaryButton(
                            onClick = {
                                isLoading = true
                                // Simulate API call
                            },
                            text = "Save Changes"
                        )
                    }
                }
            }
        }
    }
}

// ==================== Example 2: Welcome Screen ====================

/**
 * Example of a welcome/onboarding screen with gradient background.
 * Pattern: Gradient background + Animated logo + Header + Card + Buttons
 */
@Composable
fun ExampleWelcomeScreen(
    onGetStarted: () -> Unit = {},
    onLogin: () -> Unit = {}
) {
    DonorPlusGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(DonorPlusSpacing.L),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Logo
            DonorPlusAnimatedLogo(
                size = 140.dp,
                iconSize = 72.dp
            )
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            // Welcome Header
            DonorPlusHeader(
                title = "Welcome to DonorPlus",
                subtitle = "Join our community of life-savers",
                titleColor = PrimaryRed
            )
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            // Info Card
            var showCard by remember { mutableStateOf(false) }
            
            LaunchedEffect(Unit) {
                delay(600)
                showCard = true
            }
            
            DonorPlusAnimatedCard(visible = showCard) {
                Column(
                    modifier = Modifier.padding(DonorPlusSpacing.L),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Every donation counts",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = PrimaryRed
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
                    
                    Text(
                        text = "Join thousands of donors making a difference every day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
                    
                    // Get Started Button
                    DonorPlusPrimaryButton(
                        onClick = onGetStarted,
                        text = "Get Started"
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    
                    // Login Button
                    DonorPlusOutlinedButton(
                        onClick = onLogin,
                        text = "I Already Have an Account"
                    )
                }
            }
        }
    }
}

// ==================== Example 3: Dashboard Screen ====================

/**
 * Example of a main dashboard screen.
 * Pattern: Solid background + Multiple cards + Stats
 */
@Composable
fun ExampleDashboardScreen() {
    DonorPlusSolidBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            DonorPlusCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DonorPlusSpacing.M),
                backgroundColor = PrimaryRed,
                cornerRadius = 16.dp
            ) {
                Column(modifier = Modifier.padding(DonorPlusSpacing.L)) {
                    Text(
                        text = "Welcome Back!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = androidx.compose.ui.graphics.Color.White
                    )
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
                    Text(
                        text = "Ready to make a difference today?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f)
                    )
                }
            }
            
            // Stats Section
            DonorPlusContentContainer {
                Column {
                    DonorPlusSectionHeader(text = "Your Impact")
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    
                    // Stats Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(DonorPlusSpacing.M)
                    ) {
                        // Stat Card 1
                        DonorPlusCard(
                            modifier = Modifier.weight(1f),
                            cornerRadius = 16.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(DonorPlusSpacing.M),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "5",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = PrimaryRed
                                )
                                Text(
                                    text = "Donations",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        
                        // Stat Card 2
                        DonorPlusCard(
                            modifier = Modifier.weight(1f),
                            cornerRadius = 16.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(DonorPlusSpacing.M),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "15",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = SecondaryBlue
                                )
                                Text(
                                    text = "Lives Saved",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
                    
                    // Quick Actions Section
                    DonorPlusSectionHeader(text = "Quick Actions")
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    
                    DonorPlusPrimaryButton(
                        onClick = { /* Schedule donation */ },
                        text = "Schedule Donation"
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    
                    DonorPlusSecondaryButton(
                        onClick = { /* View history */ },
                        text = "View History"
                    )
                }
            }
        }
    }
}

// ==================== Example 4: List Screen ====================

/**
 * Example of a list screen with cards.
 * Pattern: Solid background + LazyColumn + Card items
 */
@Composable
fun ExampleDonationHistoryScreen() {
    // Sample data
    val donations = listOf(
        DonationItem("Blood Donation", "Jan 15, 2025", "O+ Blood Type"),
        DonationItem("Plasma Donation", "Dec 10, 2024", "Universal Donor"),
        DonationItem("Blood Donation", "Nov 5, 2024", "O+ Blood Type"),
        DonationItem("Platelet Donation", "Oct 20, 2024", "High Count"),
    )
    
    DonorPlusSolidBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(DonorPlusSpacing.M)
        ) {
            item {
                DonorPlusSectionHeader(text = "Donation History")
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                
                DonorPlusInfoMessage(
                    message = "Thank you for your ${donations.size} donations!"
                )
                
                Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
            }
            
            items(donations) { donation ->
                DonorPlusCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = DonorPlusSpacing.S),
                    cornerRadius = 16.dp
                ) {
                    Column(modifier = Modifier.padding(DonorPlusSpacing.M)) {
                        Text(
                            text = donation.type,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = PrimaryRed
                        )
                        Spacer(modifier = Modifier.height(DonorPlusSpacing.XS))
                        Text(
                            text = donation.date,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(DonorPlusSpacing.XS))
                        Text(
                            text = donation.details,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// Data class for example
data class DonationItem(
    val type: String,
    val date: String,
    val details: String
)

// ==================== Example 5: Error State Screen ====================

/**
 * Example of an error state screen.
 * Pattern: Gradient background + Error message + Retry button
 */
@Composable
fun ExampleErrorScreen(
    onRetry: () -> Unit = {}
) {
    DonorPlusGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DonorPlusSpacing.L),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DonorPlusAnimatedLogo(animate = false)
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            DonorPlusCard {
                Column(
                    modifier = Modifier.padding(DonorPlusSpacing.L),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Oops!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = PrimaryRed
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    
                    DonorPlusErrorMessage(
                        message = "Something went wrong. Please try again."
                    )
                    
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.L))
                    
                    DonorPlusPrimaryButton(
                        onClick = onRetry,
                        text = "Try Again"
                    )
                }
            }
        }
    }
}

// ==================== Example 6: Loading Screen ====================

/**
 * Example of a loading state screen.
 * Pattern: Gradient background + Animated logo + Loading indicator
 */
@Composable
fun ExampleLoadingScreen() {
    DonorPlusGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(DonorPlusSpacing.L),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DonorPlusAnimatedLogo()
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.XL))
            
            DonorPlusLoadingIndicator()
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
            
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

/**
 * USAGE NOTES:
 * 
 * 1. Copy these patterns when creating new screens
 * 2. Customize content while maintaining the structure
 * 3. Use appropriate backgrounds (Gradient for special screens, Solid for main screens)
 * 4. Always use spacing constants from DonorPlusSpacing
 * 5. Add animations where appropriate
 * 6. Handle loading and error states
 * 7. Test on different screen sizes
 * 8. Ensure accessibility (content descriptions, contrast)
 * 
 * For more details, see DESIGN_SYSTEM_GUIDE.md
 */

