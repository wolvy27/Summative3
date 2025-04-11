package com.example.summative3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.summative3.uiScreens.AddEventScreen
import com.example.summative3.uiScreens.EventListScreen
import com.example.summative3.viewmodel.EventViewModel

@Composable
fun AppNavigation(navController: NavHostController, viewModel: EventViewModel, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = Screen.AddEvent.name, modifier = modifier) {
        composable(Screen.AddEvent.name) {
            AddEventScreen(viewModel)
        }
        composable(Screen.EventList.name) {
            EventListScreen(viewModel)
        }
    }
}
