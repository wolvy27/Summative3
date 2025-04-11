package com.example.summative3.uiScreens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.summative3.data.Event
import com.example.summative3.viewmodel.EventViewModel
import java.util.*

@Composable
fun AddEventScreen(
    viewModel: EventViewModel,
    modifier: Modifier = Modifier
) {
    var eventState by remember { mutableStateOf(EventFormState()) }
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        EventInputFields(
            state = eventState,
            onNameChange = { eventState = eventState.copy(name = it) },
            onDetailsChange = { eventState = eventState.copy(details = it) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateTimeSelectionSection(
            date = eventState.date,
            time = eventState.time,
            onDateSelected = { year, month, day ->
                eventState = eventState.copy(date = "${year}-${month + 1}-${day}")
            },
            onTimeSelected = { hour, minute ->
                eventState = eventState.copy(time = String.format("%02d:%02d", hour, minute))
            },
            calendar = calendar,
            context = context,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        SaveEventButton(
            eventState = eventState,
            onSave = {
                viewModel.addEvent(eventState.toEvent())
                eventState = EventFormState() // Reset form
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EventInputFields(
    state: EventFormState,
    onNameChange: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text("Event Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.details,
            onValueChange = onDetailsChange,
            label = { Text("Event Details") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DateTimeSelectionSection(
    date: String,
    time: String,
    onDateSelected: (Int, Int, Int) -> Unit,
    onTimeSelected: (Int, Int) -> Unit,
    calendar: Calendar,
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            onClick = {
                DatePickerDialog(
                    context,
                    { _, year, month, day -> onDateSelected(year, month, day) },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (date.isEmpty()) "Pick Date" else "Date: $date")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                TimePickerDialog(
                    context,
                    { _, hour, minute -> onTimeSelected(hour, minute) },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (time.isEmpty()) "Pick Time" else "Time: $time")
        }
    }
}

@Composable
private fun SaveEventButton(
    eventState: EventFormState,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onSave,
        enabled = eventState.isValid(),
        modifier = modifier
    ) {
        Text("Save Event")
    }
}

private data class EventFormState(
    val name: String = "",
    val details: String = "",
    val date: String = "",
    val time: String = ""
) {
    fun isValid(): Boolean = name.isNotBlank() &&
            details.isNotBlank() &&
            date.isNotBlank() &&
            time.isNotBlank()

    fun toEvent(): Event = Event(
        name = name,
        details = details,
        date = date,
        time = time
    )
}