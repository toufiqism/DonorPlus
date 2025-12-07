package com.tofiq.blood.dashboard.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tofiq.blood.ui.components.DonorPlusCard
import com.tofiq.blood.ui.components.DonorPlusPrimaryButton
import com.tofiq.blood.ui.components.DonorPlusSectionHeader
import com.tofiq.blood.ui.components.DonorPlusSolidBackground
import com.tofiq.blood.ui.components.DonorPlusSpacing
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import com.tofiq.blood.ui.theme.SuccessGreen
import com.tofiq.blood.ui.theme.TextSecondary

/**
 * Home screen - Dashboard overview
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val showFeatureToast = remember {
        {
            Toast.makeText(context, "Feature under development", Toast.LENGTH_SHORT).show()
        }
    }
    
    DonorPlusSolidBackground {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Welcome Header Card
            WelcomeHeader()
            
            // Stats Section
            StatsSection()
            
            // Quick Actions Section
            QuickActionsSection(onBookDonation = showFeatureToast)
            
            // Recent Activity Section
            RecentActivitySection()
        }
    }
}

@Composable
private fun WelcomeHeader() {
    DonorPlusCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(DonorPlusSpacing.M),
        backgroundColor = PrimaryRed
    ) {
        Column(
            modifier = Modifier.padding(DonorPlusSpacing.L)
        ) {
            Text(
                text = "Welcome Back!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
            Text(
                text = "Ready to save lives today?",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun StatsSection() {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Your Impact")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DonorPlusSpacing.M)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Favorite,
                value = "12",
                label = "Donations",
                color = PrimaryRed
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.People,
                value = "36",
                label = "Lives Saved",
                color = SuccessGreen
            )
        }
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(DonorPlusSpacing.M)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.LocalHospital,
                value = "A+",
                label = "Blood Type",
                color = SecondaryBlue
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Favorite,
                value = "45",
                label = "Days Until",
                color = AccentCoral
            )
        }
    }
}

@Composable
private fun QuickActionsSection(onBookDonation: () -> Unit) {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Quick Actions")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        DonorPlusCard {
            Column(
                modifier = Modifier.padding(DonorPlusSpacing.L)
            ) {
                DonorPlusPrimaryButton(
                    onClick = onBookDonation,
                    text = "Book Donation",
                    icon = Icons.Default.Favorite
                )
                
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                
                Text(
                    text = "Find nearby donation centers and schedule your next donation.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun RecentActivitySection() {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Recent Activity")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        ActivityCard(
            title = "Blood Donation",
            date = "Dec 1, 2025",
            location = "City Hospital"
        )
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
        
        ActivityCard(
            title = "Blood Donation",
            date = "Oct 15, 2025",
            location = "Community Center"
        )
    }
}

/**
 * Stat card component for displaying statistics
 */
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    color: Color
) {
    DonorPlusCard(
        modifier = modifier,
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DonorPlusSpacing.M),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.height(32.dp)
            )
            Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

/**
 * Activity card component for displaying recent activities
 */
@Composable
private fun ActivityCard(
    title: String,
    date: String,
    location: String
) {
    DonorPlusCard(
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DonorPlusSpacing.M),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = PrimaryRed,
                modifier = Modifier.height(24.dp)
            )
            Spacer(modifier = Modifier.padding(DonorPlusSpacing.S))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$date • $location",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}
