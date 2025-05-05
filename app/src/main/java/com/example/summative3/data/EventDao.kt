package com.example.summative3.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Update
    suspend fun updateEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM event_table ORDER BY id DESC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM event_table WHERE id = :eventId")
    suspend fun getEventById(eventId: Int): Event?
}