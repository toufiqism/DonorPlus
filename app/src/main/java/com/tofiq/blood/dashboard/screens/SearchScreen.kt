package com.tofiq.blood.dashboard.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.tofiq.blood.dashboard.components.DonationCenterCard
import com.tofiq.blood.dashboard.components.DonationCenterData
import com.tofiq.blood.ui.components.DonorPlusSectionHeader
import com.tofiq.blood.ui.components.DonorPlusSolidBackground
import com.tofiq.blood.ui.components.DonorPlusSpacing
import com.tofiq.blood.ui.components.DonorPlusTextField
import com.tofiq.blood.ui.theme.TextSecondary

/**
 * Sample data - defined outside composable for stability
 */
private val sampleDonationCenters = listOf(
    DonationCenterData("1", "City Hospital Blood Bank", "123 Main Street, Downtown", "+1 (555) 123-4567", "0.5 km"),
    DonationCenterData("2", "Community Health Center", "456 Oak Avenue, Midtown", "+1 (555) 234-5678", "1.2 km"),
    DonationCenterData("3", "Red Cross Blood Center", "789 Pine Road, Uptown", "+1 (555) 345-6789", "2.3 km"),
    DonationCenterData("4", "Memorial Hospital", "321 Elm Street, Westside", "+1 (555) 456-7890", "3.1 km"),
    DonationCenterData("5", "Central Medical Center", "654 Maple Drive, Eastside", "+1 (555) 567-8901", "4.5 km")
)

/**
 * Stable state holder for SearchScreen
 */
@Stable
class SearchScreenState(
    private val context: Context
) {
    var searchQuery by mutableStateOf("")
        private set
    
    val filteredCenters by derivedStateOf {
        if (searchQuery.isBlank()) {
            sampleDonationCenters
        } else {
            sampleDonationCenters.filter { center ->
                center.name.contains(searchQuery, ignoreCase = true) ||
                center.address.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        searchQuery = query
    }
    
    fun showFeatureToast() {
        Toast.makeText(context, "Feature under development", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun rememberSearchScreenState(): SearchScreenState {
    val context = LocalContext.current
    return remember { SearchScreenState(context) }
}

/**
 * Search screen - Find donation centers
 */
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    val state = rememberSearchScreenState()
    
    DonorPlusSolidBackground {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            SearchHeader(
                searchQuery = state.searchQuery,
                onSearchQueryChange = state::updateSearchQuery,
                resultsCount = state.filteredCenters.size
            )
            
            SearchResultsList(
                centers = state.filteredCenters,
                onCenterClick = state::showFeatureToast
            )
        }
    }
}

@Composable
private fun SearchHeader(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    resultsCount: Int
) {
    Column(
        modifier = Modifier.padding(DonorPlusSpacing.M)
    ) {
        DonorPlusSectionHeader(text = "Find Donation Centers")
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        DonorPlusTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = "Search by name or location",
            leadingIcon = Icons.Default.Search
        )
        
        Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        
        ResultsCountText(count = resultsCount)
    }
}

@Composable
private fun ResultsCountText(count: Int) {
    Text(
        text = "$count centers found nearby",
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary
    )
}

@Composable
private fun SearchResultsList(
    centers: List<DonationCenterData>,
    onCenterClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = DonorPlusSpacing.M,
            end = DonorPlusSpacing.M,
            top = DonorPlusSpacing.S,
            bottom = DonorPlusSpacing.M
        )
    ) {
        items(
            items = centers,
            key = { it.id }
        ) { center ->
            DonationCenterCard(
                data = center,
                onClick = onCenterClick,
                locationIcon = Icons.Default.LocationOn,
                phoneIcon = Icons.Default.Phone
            )
            Spacer(modifier = Modifier.height(DonorPlusSpacing.M))
        }
    }
}
