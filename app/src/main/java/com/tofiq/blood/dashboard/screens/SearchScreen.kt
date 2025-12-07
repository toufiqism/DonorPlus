package com.tofiq.blood.dashboard.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tofiq.blood.ui.components.DonorPlusCard
import com.tofiq.blood.ui.components.DonorPlusSectionHeader
import com.tofiq.blood.ui.components.DonorPlusSolidBackground
import com.tofiq.blood.ui.components.DonorPlusSpacing
import com.tofiq.blood.ui.components.DonorPlusTextField
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import com.tofiq.blood.ui.theme.TextSecondary

/**
 * Data class for donation center
 */
data class DonationCenter(
    val id: String,
    val name: String,
    val address: String,
    val phone: String,
    val distance: String
)

// Sample data - moved outside composable to prevent recreation
private val sampleDonationCenters = listOf(
    DonationCenter(
        id = "1",
        name = "City Hospital Blood Bank",
        address = "123 Main Street, Downtown",
        phone = "+1 (555) 123-4567",
        distance = "0.5 km"
    ),
    DonationCenter(
        id = "2",
        name = "Community Health Center",
        address = "456 Oak Avenue, Midtown",
        phone = "+1 (555) 234-5678",
        distance = "1.2 km"
    ),
    DonationCenter(
        id = "3",
        name = "Red Cross Blood Center",
        address = "789 Pine Road, Uptown",
        phone = "+1 (555) 345-6789",
        distance = "2.3 km"
    ),
    DonationCenter(
        id = "4",
        name = "Memorial Hospital",
        address = "321 Elm Street, Westside",
        phone = "+1 (555) 456-7890",
        distance = "3.1 km"
    ),
    DonationCenter(
        id = "5",
        name = "Central Medical Center",
        address = "654 Maple Drive, Eastside",
        phone = "+1 (555) 567-8901",
        distance = "4.5 km"
    )
)

/**
 * Search screen - Find donation centers
 */
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val showFeatureToast = remember {
        {
            Toast.makeText(context, "Feature under development", Toast.LENGTH_SHORT).show()
        }
    }
    
    var searchQuery by remember { mutableStateOf("") }
    
    // Filter centers based on search query
    val filteredCenters = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            sampleDonationCenters
        } else {
            sampleDonationCenters.filter { center ->
                center.name.contains(searchQuery, ignoreCase = true) ||
                center.address.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    DonorPlusSolidBackground {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(DonorPlusSpacing.M)
            ) {
                DonorPlusSectionHeader(text = "Find Donation Centers")
                
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                
                // Search field
                DonorPlusTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = "Search by name or location",
                    leadingIcon = Icons.Default.Search
                )
                
                Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                
                // Results count
                Text(
                    text = "${filteredCenters.size} centers found nearby",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            
            // List of donation centers
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = DonorPlusSpacing.M,
                    end = DonorPlusSpacing.M,
                    top = DonorPlusSpacing.M,
                    bottom = DonorPlusSpacing.M
                )
            ) {
                items(
                    items = filteredCenters,
                    key = { it.id }
                ) { center ->
                    DonationCenterCard(
                        center = center,
                        onClick = showFeatureToast
                    )
                    Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
                }
            }
        }
    }
}

/**
 * Donation center card component
 */
@Composable
private fun DonationCenterCard(
    center: DonationCenter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
            // Header with name and distance
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = center.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = center.distance,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed
                )
            }
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
            
            // Address
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = SecondaryBlue,
                    modifier = Modifier.height(20.dp)
                )
                Spacer(modifier = Modifier.width(DonorPlusSpacing.S))
                Text(
                    text = center.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.height(DonorPlusSpacing.S))
            
            // Phone
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone",
                    tint = SecondaryBlue,
                    modifier = Modifier.height(20.dp)
                )
                Spacer(modifier = Modifier.width(DonorPlusSpacing.S))
                Text(
                    text = center.phone,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}
