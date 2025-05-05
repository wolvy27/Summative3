package com.example.summative3.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val details: String,
    val date: String,
    val time: String,
    val location: String = "", // Address of the location
    val latitude: Double = 0.0, // Latitude coordinate
    val longitude: Double = 0.0 // Longitude coordinate
)