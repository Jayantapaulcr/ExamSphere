package com.noveletytech.examsphere.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Categories,
        BottomNavItem.Favorites,
        BottomNavItem.Setting
    )

    Surface(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp // Adds a subtle tint in dark mode for better depth
    ) {
        NavigationBar(
            containerColor = Color.Transparent, // Let Surface handle the background
            tonalElevation = 0.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            items.forEach { item ->
                val selected = currentDestination?.route == item.screen_route
                
                NavigationBarItem(
                    icon = { 
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon, 
                            contentDescription = item.title 
                        ) 
                    },
                    label = { 
                        Text(
                            text = item.title,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            style = MaterialTheme.typography.labelSmall
                        ) 
                    },
                    selected = selected,
                    onClick = {
                        navController.navigate(item.screen_route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    )
                )
            }
        }
    }
}

sealed class BottomNavItem(
    val title: String, 
    val selectedIcon: ImageVector, 
    val unselectedIcon: ImageVector, 
    val screen_route: String
) {
    object Home : BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home, Screens.HOME_SCREEN)
    object Categories: BottomNavItem("Categories", Icons.Filled.List, Icons.Outlined.List, Screens.CATEGORIES_SCREEN)
    object Favorites: BottomNavItem("Favorites", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, Screens.FAVORITES_SCREEN)
    object Setting: BottomNavItem("Settings", Icons.Filled.Settings, Icons.Outlined.Settings, Screens.PROFILE_SCREEN)
}
