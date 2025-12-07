package com.tofiq.blood.dashboard.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tofiq.blood.dashboard.components.ActivityCard
import com.tofiq.blood.dashboard.components.ActivityData
import com.tofiq.blood.dashboard.components.StatData
import com.tofiq.blood.dashboard.components.StatsRow
import com.tofiq.blood.dashboard.components.WelcomeCard
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
 * Stable state holder for HomeScreen to prevent unnecessary recompositions
 */
@Stable
class HomeScreenState(
    private val context: Context
) {
    val statsRow1 = listOf(
        StatData(Icons.Default.Favorite, "12", "Donations", PrimaryRed),
        StatData(Icons.Default.People, "36", "Lives Saved", SuccessGreen)
    )
    
    val statsRow2 = listOf(
        StatData(Icons.Default.LocalHospital, "A+", "Blood Type", SecondaryBlue),
        StatData(Icons.Default.Favorite, "45", "Days Until", AccentCoral)
    )
    
    val recentActivities = listOf(
        ActivityData("Blood Donation", "Dec 1, 2025", "City Hospital"),
        ActivityData("Blood Donation", "Oct 15, 2025", "Community Center")
    )
    
    fun showFeatureToast() {
        Toast.makeText(context, "Feature under development", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun rememberHomeScreenState(): HomeScreenState {
    val context = LocalContext.current
    return remember { HomeScreenState(context) }
}

/**
 * Home screen - Dashboard overview
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    val state = rememberHomeScreenState()
    
    DonorPlusSolidBackground {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            HomeWelcomeSection()
            HomeStatsSection(state)
            HomeQuickActionsSection(onBookDonation = state::showFeatureToast)
            HomeRecentActivitySection(state)
        }
    }
}

@Composable
private fun HomeWelcomeSection() {
    WelcomeCard(
        title = "Welcome Back!",
        subtitle = "Ready to save lives today?",
        modifier = Modifier.padding(DonorPlusSpacing.M)
    )
}

@Composable
private fun HomeStatsSection(state: HomeScreenState) {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Your Impact")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        StatsRow(
            leftStat = state.statsRow1[0],
            rightStat = state.statsRow1[1]
        )
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        StatsRow(
            leftStat = state.statsRow2[0],
            rightStat = state.statsRow2[1]
        )
    }
}

@Composable
private fun HomeQuickActionsSection(onBookDonation: () -> Unit) {
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
private fun HomeRecentActivitySection(state: HomeScreenState) {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Recent Activity")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        state.recentActivities.forEachIndexed { index, activity ->
            ActivityCard(data = activity)
            if (index < state.recentActivities.lastIndex) {
                Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
            }
        }
    }
}
