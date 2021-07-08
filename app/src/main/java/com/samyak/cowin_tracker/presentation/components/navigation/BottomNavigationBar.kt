package com.samyak.cowin_tracker.presentation.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.samyak.cowin_tracker.presentation.components.navigation.NavigationItem

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Search,
        NavigationItem.Tracking
    )

    BottomNavigation(
        backgroundColor = Color.White,
        elevation = 8.dp
    ) {

        val currentRouteId = navController.currentDestination?.id
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Black,
                alwaysShowLabel = true,
                selected = currentRouteId==item.routeTo,
                onClick = {
                    navController.currentDestination?.let {
                        if (it.id != item.routeTo) {
                            navController.navigate(item.routeTo)
                        }
                    }
                })

        }
    }
}