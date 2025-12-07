package com.tofiq.blood.dashboard.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tofiq.blood.ui.components.DonorPlusCard
import com.tofiq.blood.ui.components.DonorPlusOutlinedButton
import com.tofiq.blood.ui.components.DonorPlusSectionHeader
import com.tofiq.blood.ui.components.DonorPlusSolidBackground
import com.tofiq.blood.ui.components.DonorPlusSpacing
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import com.tofiq.blood.ui.theme.TextSecondary

/**
 * Profile screen - User profile and settings
 */
@Composable
fun ProfileScreen(
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
            // Profile Header
            ProfileHeader(onEditClick = showFeatureToast)
            
            // Profile Information Section
            PersonalInfoSection()
            
            // Settings Section
            SettingsSection(
                onNotificationsClick = showFeatureToast,
                onAccountSettingsClick = showFeatureToast
            )
            
            // Logout Button
            LogoutSection(onLogoutClick = showFeatureToast)
        }
    }
}

@Composable
private fun ProfileHeader(onEditClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(PrimaryRed, AccentCoral)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    modifier = Modifier.size(60.dp),
                    tint = PrimaryRed
                )
            }
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
            
            Text(
                text = "John Doe",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "Blood Type: A+",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
        
        // Edit button
        IconButton(
            onClick = onEditClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(DonorPlusSpacing.M)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun PersonalInfoSection() {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Personal Information")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        DonorPlusCard {
            Column(
                modifier = Modifier.padding(DonorPlusSpacing.M)
            ) {
                ProfileInfoItem(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = "john.doe@example.com"
                )
                
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                
                ProfileInfoItem(
                    icon = Icons.Default.Phone,
                    label = "Phone",
                    value = "+1 (555) 123-4567"
                )
                
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                
                ProfileInfoItem(
                    icon = Icons.Default.LocalHospital,
                    label = "Blood Type",
                    value = "A+ (Positive)"
                )
                
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                
                ProfileInfoItem(
                    icon = Icons.Default.Favorite,
                    label = "Total Donations",
                    value = "12 times"
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    onNotificationsClick: () -> Unit,
    onAccountSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Settings")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        DonorPlusCard {
            Column(
                modifier = Modifier.padding(DonorPlusSpacing.M)
            ) {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Manage notification preferences",
                    onClick = onNotificationsClick
                )
                
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                
                SettingsItem(
                    icon = Icons.Default.Settings,
                    title = "Account Settings",
                    subtitle = "Privacy and security",
                    onClick = onAccountSettingsClick
                )
            }
        }
    }
}

@Composable
private fun LogoutSection(onLogoutClick: () -> Unit) {
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

/**
 * Profile information item component
 */
@Composable
private fun ProfileInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = SecondaryBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.padding(DonorPlusSpacing.S))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Settings item component
 */
@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = SecondaryBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.padding(DonorPlusSpacing.S))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
        IconButton(onClick = onClick) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = SecondaryBlue
            )
        }
    }
}
