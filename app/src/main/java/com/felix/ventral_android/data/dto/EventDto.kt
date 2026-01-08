package com.felix.ventral_android.data.dto

import com.felix.ventral_android.domain.model.Category
import com.felix.ventral_android.domain.model.Event
import com.felix.ventral_android.utils.formatIsoToHumanReadable
import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("event_id")
    val eventId: String,

    @SerializedName("author_id")
    val authorId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("date_start")
    val dateStart: String,

    @SerializedName("date_end")
    val dateEnd: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("quota")
    val quota: Int,

    @SerializedName("images")
    val images: List<EventImage>,

    @SerializedName("categories")
    val categories: List<EventCategory>,

    @SerializedName("_count")
    val count: EventCountDto,

    @SerializedName("address")
    val address: String,

    @SerializedName("city")
    val city: String
)

data class EventCountDto(
    @SerializedName("applications")
    val applications: Int,

    @SerializedName("likes")
    val likes: Int,

    @SerializedName("dislikes")
    val dislikes: Int
)

data class CreateEventRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("date_start") val dateStart: String,
    @SerializedName("date_end") val dateEnd: String,
    @SerializedName("price") val price: Int,
    @SerializedName("quota") val quota: Int?,
    @SerializedName("status") val status: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("categories") val categories: List<String>,
    @SerializedName("address") val address: String,
    @SerializedName("city") val city: String
)

data class UpdateEventRequestDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("date_start") val dateStart: String? = null,
    @SerializedName("date_end") val dateEnd: String? = null,
    @SerializedName("price") val price: Int? = null,
    @SerializedName("quota") val quota: Int? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("images") var images: List<String>? = null,
    @SerializedName("categories") val categories: List<String>? = null,
    @SerializedName("address") val address: String?,
    @SerializedName("city") val city: String?
)

data class EventImage(
    @SerializedName("imageEvent_id") val id: String,
    @SerializedName("event_id") val eventId: String,
    @SerializedName("img_url") val url: String
)

data class EventCategory(
    @SerializedName("id") val id: String,
    @SerializedName("event_id") val eventId: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("category") val category: CategoryDto,

)

fun EventDto.toDomain(): Event {
    return Event(
        id = this.eventId,
        authorId = this.authorId,
        name = this.name,
        description = this.description,
        dateStart = formatIsoToHumanReadable(this.dateStart),
        dateEnd = formatIsoToHumanReadable(this.dateEnd),
        price = this.price,
        status = this.status,
        quota = this.quota,
        images = this.images.map{it.url},
        categories = this.categories.map { it.category.toDomain() },
        likes = this.count.likes,
        address = this.address,
        city = this.city
    )
}
