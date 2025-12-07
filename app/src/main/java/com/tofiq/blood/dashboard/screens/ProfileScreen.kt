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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tofiq.blood.dashboard.components.ProfileHeader
import com.tofiq.blood.dashboard.components.ProfileInfoData
import com.tofiq.blood.dashboard.components.ProfileInfoItem
import com.tofiq.blood.dashboard.components.SettingsItem
import com.tofiq.blood.dashboard.components.SettingsItemData
import com.tofiq.blood.ui.components.DonorPlusCard
import com.tofiq.blood.ui.components.DonorPlusOutlinedButton
import com.tofiq.blood.ui.components.DonorPlusSectionHeader
import com.tofiq.blood.ui.components.DonorPlusSolidBackground
import com.tofiq.blood.ui.components.DonorPlusSpacing
import com.tofiq.blood.ui.theme.PrimaryRed

/**
 * Stable state holder for ProfileScreen
 */
@Stable
class ProfileScreenState(
    private val context: Context
) {
    val userName = "John Doe"
    val bloodType = "A+"
    
    val profileInfoItems = listOf(
        ProfileInfoData(Icons.Default.Email, "Email", "john.doe@example.com"),
        ProfileInfoData(Icons.Default.Phone, "Phone", "+1 (555) 123-4567"),
        ProfileInfoData(Icons.Default.LocalHospital, "Blood Type", "A+ (Positive)"),
        ProfileInfoData(Icons.Default.Favorite, "Total Donations", "12 times")
    )
    
    val settingsItems = listOf(
        SettingsItemData(Icons.Default.Notifications, "Notifications", "Manage notification preferences"),
        SettingsItemData(Icons.Default.Settings, "Account Settings", "Privacy and security")
    )
    
    fun showFeatureToast() {
        Toast.makeText(context, "Feature under development", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun rememberProfileScreenState(): ProfileScreenState {
    val context = LocalContext.current
    return remember { ProfileScreenState(context) }
}

/**
 * Profile screen - User profile and settings
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val state = rememberProfileScreenState()
    
    DonorPlusSolidBackground {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeader(
                name = state.userName,
                bloodType = state.bloodType,
                onEditClick = state::showFeatureToast
            )
            
            ProfilePersonalInfoSection(state)
            ProfileSettingsSection(state)
            ProfileLogoutSection(onLogoutClick = state::showFeatureToast)
        }
    }
}

@Composable
private fun ProfilePersonalInfoSection(state: ProfileScreenState) {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Personal Information")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        DonorPlusCard {
            Column(
                modifier = Modifier.padding(DonorPlusSpacing.M)
            ) {
                state.profileInfoItems.forEachIndexed { index, item ->
                    ProfileInfoItem(data = item)
                    if (index < state.profileInfoItems.lastIndex) {
                        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSettingsSection(state: ProfileScreenState) {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Settings")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        DonorPlusCard {
            Column(
                modifier = Modifier.padding(DonorPlusSpacing.M)
            ) {
                state.settingsItems.forEachIndexed { index, item ->
                    SettingsItem(
                        data = item,
                        onClick = state::showFeatureToast
                    )
                    if (index < state.settingsItems.lastIndex) {
                        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileLogoutSection(onLogoutClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusOutlinedButton(
            onClick = onLogoutClick,
            text = "Logout",
            icon = Icons.Default.Logout,
            borderColor = PrimaryRed,
            textColor = PrimaryRed
        )
    }
}
