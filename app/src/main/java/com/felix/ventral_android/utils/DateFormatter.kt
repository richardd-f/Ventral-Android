package com.felix.ventral_android.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
