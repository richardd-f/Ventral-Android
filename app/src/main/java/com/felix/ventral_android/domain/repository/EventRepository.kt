package com.felix.ventral_android.domain.repository

import com.felix.ventral_android.domain.model.Event

interface EventRepository {
    // Get all events for the general feed
    suspend fun getAllEvents(): Result<List<Event>>

    // Get events created by a specific user (for the Profile screen)
    suspend fun getEventsByUserId(userId: String): Result<List<Event>>

    // Create a new event
    suspend fun createEvent(event: Event): Result<Event>

    // Update an existing event
    suspend fun updateEvent(eventId: String, updates: Map<String, Any>): Result<Event>

    // Delete an event
    suspend fun deleteEvent(eventId: String): Result<Unit>
}