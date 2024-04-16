package com.github.elment.sample.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.elment.sample.ui.case1.Case1Screen
import com.github.elment.sample.ui.navigation.MainScreenTabs

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentNavDestination = navBackStackEntry?.destination
            NavigationBar {
                MainScreenTabs.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentNavDestination?.hierarchy?.any { it.route == tab.route } == true,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = when (tab) {
                                    MainScreenTabs.Case1 -> Icons.Filled.Home
                                    MainScreenTabs.Case2 -> Icons.Filled.Favorite
                                    MainScreenTabs.Case3 -> Icons.Filled.AddCard
                                    MainScreenTabs.Case4 -> Icons.Filled.Alarm
                                },
                                contentDescription = null
                            )
                        },
                        label = { Text(text = tab.title) },
                        alwaysShowLabel = false
                    )
                }
            }
        },
        modifier = Modifier.systemBarsPadding()
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = MainScreenTabs.Case1.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainScreenTabs.Case1.route) { Case1Screen() }
            composable(MainScreenTabs.Case2.route) { }
            composable(MainScreenTabs.Case3.route) {

            }
            composable(MainScreenTabs.Case4.route) {

            }
        }
    }
}