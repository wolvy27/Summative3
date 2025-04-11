package com.example.summative3.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.summative3.navigation.AppNavigation
import com.example.summative3.navigation.Screen
import com.example.summative3.viewmodel.EventViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: EventViewModel) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val items = listOf(Screen.AddEvent, Screen.EventList)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                items.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(screen.name) },
                        selected = false,
                        onClick = {
                            navController.navigate(screen.name)
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
                                if (screen == Screen.AddEvent)
                                    Icon(Icons.Default.Add, contentDescription = null)
                                else
                                    Icon(Icons.Default.List, contentDescription = null)
                            },
                            label = { Text(screen.name) },
                            selected = false,
                            onClick = {
                                navController.navigate(screen.name)
                            }
                        )
                    }
                }
            }
        ) { innerPadding ->
            AppNavigation(
                navController = navController,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}