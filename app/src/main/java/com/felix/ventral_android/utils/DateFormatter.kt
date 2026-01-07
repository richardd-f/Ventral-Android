package com.felix.ventral_android.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun toIsoString(
    date: String,
    time: String,
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val localDate = LocalDate.parse(date)   // yyyy-MM-dd
    val localTime = LocalTime.parse(time)   // HH:mm

    // Local datetime in user's timezone
    val zonedDateTime = ZonedDateTime.of(
        localDate,
        localTime,
        zoneId
    )

    // Convert to UTC
    val utcDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC)

    // Format exactly: 2025-12-16T14:30:00Z
    return utcDateTime.format(
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
    )
}

fun formatIsoToHumanReadable(isoString: String): String {
    return try {
        // 1. Parse the ISO string (assuming it's in UTC 'Z')
        val utcDateTime = ZonedDateTime.parse(isoString)

        // 2. Convert to System Default timezone (e.g., WIB for Surabaya)
        val localDateTime = utcDateTime.withZoneSameInstant(java.time.ZoneId.systemDefault())

        // 3. Define an elegant pattern
        val formatter = DateTimeFormatter.ofPattern("d MMM yyyy, HH:mm", Locale.getDefault())

        localDateTime.format(formatter)
    } catch (e: Exception) {
        isoString // Fallback to original string if parsing fails
    }
}

fun fromHumanToDateIso(humanDate: String, inputPattern: String = "d MMM yyyy"): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern(inputPattern, Locale.getDefault())
        val date = LocalDate.parse(humanDate, formatter)

        // Returns "yyyy-MM-dd"
        date.format(DateTimeFormatter.ISO_LOCAL_DATE)
    } catch (e: Exception) {
        "" // Or handle error accordingly
    }
}

fun fromHumanToTime(humanTime: String, inputPattern: String = "HH:mm"): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern(inputPattern)
        val time = LocalTime.parse(humanTime, formatter)

        // Returns "HH:mm"
        time.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        ""
    }
}