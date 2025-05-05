package com.example.summative3.uiScreens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.summative3.data.Event
import com.example.summative3.viewmodel.EventViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

@Composable
fun AddEventScreen(
    viewModel: EventViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    eventId: Int? = null
) {
    // Load existing event if in update mode
    val existingEvent by produceState<Event?>(initialValue = null) {
        if (eventId != null) {
            value = withContext(Dispatchers.IO) {
                viewModel.getEventById(eventId)
            }
        }
    }

    var eventState by remember {
        mutableStateOf(
            existingEvent?.toFormState() ?: EventFormState()
        )
    }

    // Rest of your composable remains the same...
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
            onLocationChange = { eventState = eventState.copy(location = it) },
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
                if (existingEvent != null) {
                    viewModel.updateEvent(eventState.toEvent(existingEvent!!.id))
                } else {
                    viewModel.addEvent(eventState.toEvent())
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            isUpdate = existingEvent != null
        )
    }
}

@Composable
private fun EventInputFields(
    state: EventFormState,
    onNameChange: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
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

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.location,
            onValueChange = onLocationChange,
            label = { Text("Event Location") },
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
    modifier: Modifier = Modifier,
    isUpdate: Boolean = false
) {
    Button(
        onClick = onSave,
        enabled = eventState.isValid(),
        modifier = modifier
    ) {
        Text(if (isUpdate) "Update Event" else "Save Event")
    }
}

data class EventFormState(
    val name: String = "",
    val details: String = "",
    val location: String = "",
    val date: String = "",
    val time: String = ""
) {
    fun isValid(): Boolean = name.isNotBlank() &&
            details.isNotBlank() &&
            location.isNotBlank() &&
            date.isNotBlank() &&
            time.isNotBlank()

    fun toEvent(id: Int = 0): Event = Event(
        id = id,
        name = name,
        details = details,
        date = date,
        time = time,
        location = location
    )
}

fun Event.toFormState(): EventFormState {
    return EventFormState(
        name = this.name,
        details = this.details,
        location = this.location,
        date = this.date,
        time = this.time
    )
}