package com.tofiq.blood.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tofiq.blood.ui.components.DonorPlusCard
import com.tofiq.blood.ui.components.DonorPlusSpacing
import com.tofiq.blood.ui.theme.AccentCoral
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import com.tofiq.blood.ui.theme.TextSecondary

/**
 * Immutable data classes for stable recomposition
 */
@Immutable
data class StatData(
    val icon: ImageVector,
    val value: String,
    val label: String,
    val color: Color
)

@Immutable
data class ActivityData(
    val title: String,
    val date: String,
    val location: String
)

@Immutable
data class DonationCenterData(
    val id: String,
    val name: String,
    val address: String,
    val phone: String,
    val distance: String
)

@Immutable
data class ProfileInfoData(
    val icon: ImageVector,
    val label: String,
    val value: String
)

@Immutable
data class SettingsItemData(
    val icon: ImageVector,
    val title: String,
    val subtitle: String
)

/**
 * Stat card - displays a single statistic
 */
@Composable
fun StatCard(
    data: StatData,
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp
) {
    DonorPlusCard(
        modifier = modifier,
        elevation = elevation
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DonorPlusSpacing.M),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = data.label,
                tint = data.color,
                modifier = Modifier.height(32.dp)
            )
            Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
            Text(
                text = data.value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = data.color
            )
            Text(
                text = data.label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

/**
 * Stats row - displays two stat cards side by side
 */
@Composable
fun StatsRow(
    leftStat: StatData,
    rightStat: StatData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(DonorPlusSpacing.M)
    ) {
        StatCard(
            data = leftStat,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            data = rightStat,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Activity card - displays a single activity item
 */
@Composable
fun ActivityCard(
    data: ActivityData,
    modifier: Modifier = Modifier
) {
    DonorPlusCard(
        modifier = modifier,
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
            Spacer(modifier = Modifier.width(DonorPlusSpacing.S))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${data.date} • ${data.location}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

/**
 * Donation center card - displays donation center info
 */
@Composable
fun DonationCenterCard(
    data: DonationCenterData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    locationIcon: ImageVector,
    phoneIcon: ImageVector
) {
    DonorPlusCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(DonorPlusSpacing.M)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = data.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = data.distance,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed
                )
            }
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
            
            IconTextRow(
                icon = locationIcon,
                text = data.address
            )
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
            
            IconTextRow(
                icon = phoneIcon,
                text = data.phone
            )
        }
    }
}

/**
 * Icon with text row - reusable component
 */
@Composable
fun IconTextRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    iconTint: Color = SecondaryBlue,
    textColor: Color = TextSecondary
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.height(20.dp)
        )
        Spacer(modifier = Modifier.width(DonorPlusSpacing.S))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

/**
 * Profile header with gradient background
 */
@Composable
fun ProfileHeader(
    name: String,
    bloodType: String,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
            ProfileAvatar()
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
            
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Text(
                text = "Blood Type: $bloodType",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
        
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

/**
 * Profile avatar - extracted for recomposition optimization
 */
@Composable
private fun ProfileAvatar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
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
}

/**
 * Profile info item - displays label and value with icon
 */
@Composable
fun ProfileInfoItem(
    data: ProfileInfoData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = data.icon,
            contentDescription = data.label,
            tint = SecondaryBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(DonorPlusSpacing.S))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = data.label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
            Text(
                text = data.value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Settings item - displays setting with edit button
 */
@Composable
fun SettingsItem(
    data: SettingsItemData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = data.icon,
            contentDescription = data.title,
            tint = SecondaryBlue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(DonorPlusSpacing.S))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = data.subtitle,
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

/**
 * Welcome card header
 */
@Composable
fun WelcomeCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    DonorPlusCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = PrimaryRed
    ) {
        Column(
            modifier = Modifier.padding(DonorPlusSpacing.L)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}
