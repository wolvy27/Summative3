package com.example.summative3.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summative3.data.Event
import com.example.summative3.data.EventDao
import com.example.summative3.utils.NotificationHelper
import kotlinx.coroutines.launch

class EventViewModel(
    private val eventDao: EventDao,
    private val applicationContext: Context
) : ViewModel() {

    val eventList = eventDao.getAllEvents()

    suspend fun getEventById(id: Int): Event? {
        return try {
            eventDao.getEventById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventDao.insertEvent(event)
            NotificationHelper(applicationContext).showNotification(
                "New Event Created",
                "${event.name}\n${event.details}"
            )
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            eventDao.updateEvent(event)
            NotificationHelper(applicationContext).showNotification(
                "Event Updated",
                "${event.name}\n${event.details}"
            )
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventDao.deleteEvent(event)
            NotificationHelper(applicationContext).showNotification(
                "Event Deleted",
                "${event.name} was removed"
            )
        }
    }
}