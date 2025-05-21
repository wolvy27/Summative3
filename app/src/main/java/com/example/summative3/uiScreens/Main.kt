package com.example.summative3.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.summative3.navigation.AppNavigation
import com.example.summative3.navigation.Screen
import com.example.summative3.viewmodel.EventViewModel
import com.example.summative3.viewmodel.MapViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    eventViewModel: EventViewModel,
    mapViewModel: MapViewModel
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route?.substringBefore("?")?.substringBefore("/")
    val items = listOf(Screen.AddEvent, Screen.EventList, Screen.EventMap, Screen.Calculator)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                items.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(screen.name) },
                        selected = currentRoute == screen.name,
                        onClick = {
                            navController.navigate(screen.name) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("To-Do List") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.List, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    items.forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                when (screen) {
                                    Screen.AddEvent -> Icon(Icons.Default.Add, contentDescription = null)
                                    Screen.EventList -> Icon(Icons.Default.List, contentDescription = null)
                                    Screen.EventMap -> Icon(Icons.Default.Place, contentDescription = null)
                                    Screen.Calculator -> Icon(Icons.Default.Calculate, contentDescription = null
                                    )
                                }
                            },
                            label = { Text(screen.name) },
                            selected = currentRoute == screen.name,
                            onClick = {
                                navController.navigate(screen.name) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            AppNavigation(
                navController = navController,
                eventViewModel = eventViewModel,
                mapViewModel = mapViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}