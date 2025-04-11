package com.example.summative3.uiScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.summative3.data.Event
import com.example.summative3.viewmodel.EventViewModel

@Composable
fun EventListScreen(viewModel: EventViewModel) {
    val eventList = viewModel.eventList.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(eventList.value) { event ->
            EventCard(event = event, onEventDone = { viewModel.deleteEvent(event) })
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun EventCard(event: Event, onEventDone: () -> Unit) {
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
            }
            Checkbox(
                checked = false,
                onCheckedChange = { onEventDone() }
            )
        }
    }
}