package com.example.summative3.uiScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.summative3.data.Event
import com.example.summative3.navigation.Screen
import com.example.summative3.viewmodel.EventViewModel

@Composable
fun EventListScreen(
    viewModel: EventViewModel,
    navController: NavHostController
) {
    val eventList = viewModel.eventList.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(eventList.value) { event ->
            EventCard(
                event = event,
                onEventDone = { viewModel.deleteEvent(event) },
                onUpdateClick = {
                    navController.navigate("${Screen.AddEvent.name}/${event.id}")
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EventCard(event: Event, onEventDone: () -> Unit, onUpdateClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = event.name, style = MaterialTheme.typography.titleLarge)
                Text(text = event.details)
                Text(text = "${event.date} at ${event.time}")
                Text(text = event.location, style = MaterialTheme.typography.bodySmall)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onUpdateClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Update")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Checkbox(
                    checked = false,
                    onCheckedChange = { onEventDone() }
                )
            }
        }
    }
}