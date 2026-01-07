package com.felix.ventral_android.data.dto

import com.felix.ventral_android.domain.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryDto(
    @SerializedName("category_id") val id: String,
    @SerializedName("category") val category: String
)

fun CategoryDto.toDomain(): Category {
    return Category(
        id = this.id,
        category = this.category
    )
}