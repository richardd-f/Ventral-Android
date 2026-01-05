package com.felix.ventral_android.data.repository

import com.felix.ventral_android.data.dto.CreateEventRequestDto
import com.felix.ventral_android.data.dto.toDomain
import com.felix.ventral_android.data.local.LocalDataStore
import com.felix.ventral_android.data.network.api.EventApiService
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.domain.repository.EventRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val apiService: EventApiService,
    override val localDataStore: LocalDataStore
) : BaseRepository(), EventRepository {

    override suspend fun getAllEvents(): Result<List<Event>> {
        return handleApiCall(
            call = { apiService.getAllEvents() },
            map = { list -> list.map { it.toDomain() } }
        )
    }

    override suspend fun getEventsByUserId(userId: String): Result<List<Event>> {
        return handleApiCall(
            call = { apiService.getEventsByUserId(userId) },
            map = { list -> list.map { it.toDomain() } }
        )
    }

    override suspend fun createEvent(event: Event): Result<Event> {
        val requestDto = CreateEventRequestDto(
            name = event.name,
            description = event.description,
            dateStart = event.dateStart,
            dateEnd = event.dateEnd,
            price = event.price,
            quota = event.quota,
            status = event.status
        )

        return handleApiCall(
            call = { apiService.createEvent(requestDto) },
            map = { dto -> dto.toDomain() }
        )
    }

    override suspend fun updateEvent(eventId: String, updates: Map<String, Any>): Result<Event> {
        return handleApiCall(
            call = { apiService.updateEvent(eventId, updates) },
            map = { dto -> dto.toDomain() }
        )
    }

    override suspend fun deleteEvent(eventId: String): Result<Unit> {
        return handleApiCall(
            call = { apiService.deleteEvent(eventId) },
            map = { _ -> Unit }
        )
    }

}