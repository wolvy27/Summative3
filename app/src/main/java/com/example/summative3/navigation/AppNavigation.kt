package com.example.summative3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.summative3.uiScreens.AddEventScreen
import com.example.summative3.uiScreens.CalculatorScreen
import com.example.summative3.uiScreens.EventListScreen
import com.example.summative3.uiScreens.MapScreen
import com.example.summative3.viewmodel.EventViewModel
import com.example.summative3.viewmodel.MapViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    eventViewModel: EventViewModel,
    mapViewModel: MapViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.EventList.name,
        modifier = modifier
    ) {
        // For adding new events
        composable(Screen.AddEvent.name) {
            AddEventScreen(
                viewModel = eventViewModel,
                navController = navController,
                eventId = null
            )
        }

        // For updating existing events
        composable(
            route = "${Screen.AddEvent.name}/{eventId}",
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId")
            AddEventScreen(
                viewModel = eventViewModel,
                navController = navController,
                eventId = eventId
            )
        }

        composable(Screen.EventList.name) {
            EventListScreen(
                viewModel = eventViewModel,
                navController = navController
            )
        }

        composable(Screen.EventMap.name) {
            MapScreen(
                viewModel = mapViewModel
            )
        }

        composable(Screen.Calculator.name) {
            CalculatorScreen(navController)
        }
    }
}