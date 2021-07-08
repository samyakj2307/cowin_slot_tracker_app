package com.samyak.cowin_tracker.presentation.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val icon: ImageVector, val title: String) {
    object Search : NavigationItem("search", Icons.Outlined.Search,"Search")
    object Tracking : NavigationItem("tracking", Icons.Outlined.Notifications,"Tracking")
}