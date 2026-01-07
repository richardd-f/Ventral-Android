package com.felix.ventral_android.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Event(
    val id: String?,
    val authorId: String,
    val name: String,
    val description: String,
    val dateStart: String,
    val dateEnd: String,
    val price: Int,
    val status: String, // Matches "OPEN", "CLOSED", "SUSPENDED" from Zod
    val quota: Int?,
    val images: List<String>,
    val categories: List<Category>,
    val likes: Int
) : Parcelable