package com.felix.ventral_android.data.dto

import com.felix.ventral_android.domain.model.Event
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
    val images: List<String>,

    @SerializedName("categories")
    val categories: List<String>,

    @SerializedName("_count")
    val count: EventCountDto
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
    @SerializedName("status") val status: String
)



fun EventDto.toDomain(): Event {
    return Event(
        id = this.eventId,
        authorId = this.authorId,
        name = this.name,
        description = this.description,
        dateStart = this.dateStart,
        dateEnd = this.dateEnd,
        price = this.price,
        status = this.status,
        quota = this.quota,
        images = this.images,
        categories = this.categories,
        likes = this.count.likes
    )
}