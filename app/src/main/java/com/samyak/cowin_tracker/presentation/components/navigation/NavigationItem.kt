package com.samyak.cowin_tracker.presentation.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.samyak.cowin_tracker.R

sealed class NavigationItem(val routeTo: Int, val icon: ImageVector, val title: String) {
    object Search : NavigationItem(R.id.search, Icons.Outlined.Search,"Search")
    object Tracking : NavigationItem(R.id.track, Icons.Outlined.Notifications,"Tracking")
}