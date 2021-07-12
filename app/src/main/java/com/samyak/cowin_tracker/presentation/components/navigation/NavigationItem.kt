package com.samyak.cowin_tracker.presentation.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.samyak.cowin_tracker.R

sealed class NavigationItem(
    val routeLabel: String,
    val routeId: Int,
    val icon: ImageVector,
    val title: String
) {
    object Search : NavigationItem("CenterListFragment", R.id.search, Icons.Outlined.Search, "Search")
    object Tracking : NavigationItem("PincodeListFragment", R.id.track, Icons.Outlined.Notifications, "Tracking")
}