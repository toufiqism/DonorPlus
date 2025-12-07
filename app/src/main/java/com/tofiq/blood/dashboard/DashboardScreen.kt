package com.tofiq.blood.dashboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.tofiq.blood.dashboard.screens.HomeScreen
import com.tofiq.blood.dashboard.screens.ProfileScreen
import com.tofiq.blood.dashboard.screens.SearchScreen
import com.tofiq.blood.ui.theme.PrimaryRed
import com.tofiq.blood.ui.theme.SecondaryBlue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Immutable bottom navigation item data class
 */
@Immutable
data class BottomNavItem(
    val index: Int,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * Static navigation items - defined outside composable for stability
 */
private val bottomNavItems = listOf(
    BottomNavItem(0, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(1, "Search", Icons.Filled.Search, Icons.Outlined.Search),
    BottomNavItem(2, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
)

/**
 * Main dashboard screen with bottom navigation and swipe navigation
 */
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            DashboardBottomBar(
                pagerState = pagerState,
                scope = scope
            )
        }
    ) { innerPadding ->
        DashboardPager(
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

/**
 * Bottom navigation bar - extracted for recomposition optimization
 */
@Composable
private fun DashboardBottomBar(
    pagerState: PagerState,
    scope: CoroutineScope
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = PrimaryRed
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = pagerState.currentPage == item.index
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title) },
                selected = isSelected,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(item.index)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryRed,
                    selectedTextColor = PrimaryRed,
                    unselectedIconColor = SecondaryBlue.copy(alpha = 0.6f),
                    unselectedTextColor = SecondaryBlue.copy(alpha = 0.6f),
                    indicatorColor = PrimaryRed.copy(alpha = 0.1f)
                )
            )
        }
    }
}

/**
 * Horizontal pager for screen content - extracted for recomposition optimization
 */
@Composable
private fun DashboardPager(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        beyondViewportPageCount = 0,
        userScrollEnabled = true
    ) { page ->
        when (page) {
            0 -> HomeScreen()
            1 -> SearchScreen()
            2 -> ProfileScreen()
        }
    }
}
