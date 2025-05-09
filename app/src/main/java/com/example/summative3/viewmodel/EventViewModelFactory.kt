package com.example.summative3.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.summative3.data.EventDao

class EventViewModelFactory(
    private val eventDao: EventDao,
    private val applicationContext: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventViewModel(eventDao, applicationContext) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}