package com.example.summative3.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.summative3.data.Event
import com.example.summative3.data.EventDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    val eventList: Flow<List<Event>> = eventDao.getAllEvents()

    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventDao.insertEvent(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventDao.deleteEvent(event)
        }
    }
}
