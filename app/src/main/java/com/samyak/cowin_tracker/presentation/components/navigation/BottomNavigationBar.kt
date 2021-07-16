package com.samyak.cowin_tracker.presentation.components.navigation

import android.util.Log
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.samyak.cowin_tracker.TAG

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Search,
        NavigationItem.Tracking
    )

    BottomNavigation(
        backgroundColor = Color.White,
        elevation = 8.dp,
        contentColor = Color.Black
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.label


        val isAtSearch = mutableStateOf(true)

        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Black,
                alwaysShowLabel = true,
                selected = currentRoute == item.routeLabel,
                onClick = {
                    //TODO this
                    navController.popBackStack(navController.graph.startDestinationId,false)

                    isAtSearch.value = item.title == "Search"
                    navController.currentDestination?.let {
                        if (it.id != item.routeId) {
                            navController.navigate(item.routeId)
                        }
                    }
                })

        }
    }
}