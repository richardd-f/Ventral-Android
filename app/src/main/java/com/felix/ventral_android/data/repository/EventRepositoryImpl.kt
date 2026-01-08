package com.felix.ventral_android.data.repository

import android.net.Uri
import com.felix.ventral_android.data.dto.CreateEventRequestDto
import com.felix.ventral_android.data.dto.UpdateEventRequestDto
import com.felix.ventral_android.data.dto.toDomain
import com.felix.ventral_android.data.local.LocalDataStore
import com.felix.ventral_android.data.network.api.EventApiService
import com.felix.ventral_android.data.network.cloudinary.CloudinaryManager
import com.felix.ventral_android.domain.model.Category
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.domain.repository.EventRepository
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val apiService: EventApiService,
    override val localDataStore: LocalDataStore,
    private val cloudinaryManager: CloudinaryManager
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

    override suspend fun createEvent(event: CreateEventRequestDto): Result<Event> {
        val uploadedImages = mutableListOf<String>()

        try {
            for (imgUri in event.images) {
                if (imgUri.isNotEmpty()) {
                    val uri = Uri.parse(imgUri)
                    val uploadedUrl = cloudinaryManager.uploadImage(uri) // suspending upload
                    uploadedImages.add(uploadedUrl)
                }
            }

            // If no images were uploaded, use a default image
            if (uploadedImages.isEmpty()) {
                uploadedImages.add("https://res.cloudinary.com/demo/image/upload/v1312461204/sample.jpg")
            }

        } catch (e: Exception) {
            return Result.failure(Exception("Image upload failed: ${e.message}"))
        }

        // 2️⃣ Prepare the DTO with uploaded URLs
        val requestDto = CreateEventRequestDto(
            name = event.name,
            description = event.description,
            dateStart = event.dateStart,
            dateEnd = event.dateEnd,
            price = event.price,
            quota = event.quota,
            status = event.status,
            images = uploadedImages,
            categories = event.categories
        )

        // 3️⃣ Call API with handleApiCall
        return handleApiCall(
            call = { apiService.createEvent(requestDto) },
            map = { dto -> dto.toDomain() }
        )
    }

    override suspend fun updateEvent(eventId: String, updateRequest: UpdateEventRequestDto ): Result<Event> {
        // extract only URI on images (considered as new image)
        val uriImages = updateRequest.images
            ?.filter { !it.startsWith("http") }
            ?.takeIf { it.isNotEmpty() }

        val nonUriImages = updateRequest.images
            ?.filter { it.startsWith("http://") || it.startsWith("https://") }
            ?.takeIf { it.isNotEmpty() }

        // upload new image to cloudinary
        val uploadedImages = try {
            uriImages
                ?.map { imgUri ->
                    val uri = Uri.parse(imgUri)
                    cloudinaryManager.uploadImage(uri) // suspend
                }
                ?.takeIf { it.isNotEmpty() }
                ?: emptyList()

        } catch (e: Exception) {
            return Result.failure(Exception("Image upload failed: ${e.message}"))
        }

        // combine old url, and new url
        val finalImageUrls = (nonUriImages.orEmpty() + uploadedImages)
                .takeIf { it.isNotEmpty() }

        if(updateRequest.images != null){
            updateRequest.images = finalImageUrls
        }else{
            updateRequest.images = null
        }

        return handleApiCall(
            call = { apiService.updateEvent(eventId, updateRequest) },
            map = { dto -> dto.toDomain() }
        )
    }

    override suspend fun deleteEvent(eventId: String): Result<Unit> {
        return handleApiCall(
            call = { apiService.deleteEvent(eventId) },
            map = { _ -> Unit }
        )
    }

    override suspend fun getAllCategories(): Result<List<Category>> {
        return handleApiCall(
            call = { apiService.getAllCategories() },
            map = { list -> list.map { it.toDomain() } }
        )
    }

    override suspend fun applyEvent(eventId: String): Result<Unit> {
        return handleApiCall(
            call = {apiService.applyEvent(eventId)},
            map = { application -> Unit}
        )
    }
}