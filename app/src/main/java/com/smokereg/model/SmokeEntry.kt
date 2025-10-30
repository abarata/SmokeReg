package com.smokereg.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Data class representing a single smoke entry
 * @property id Unique identifier for the entry
 * @property dateTime ISO 8601 formatted date and time string
 * @property isAvoidable Whether the smoke was avoidable
 */
@Serializable
data class SmokeEntry(
    val id: String = UUID.randomUUID().toString(),
    val dateTime: String, // ISO 8601 format (e.g., "2025-10-28T14:30:00")
    val isAvoidable: Boolean = false
) {
    /**
     * Get the time portion of the dateTime as HH:MM format
     */
    fun getTimeDisplay(): String {
        return try {
            val timePart = dateTime.split("T").getOrNull(1) ?: return ""
            val parts = timePart.split(":")
            if (parts.size >= 2) {
                "${parts[0]}:${parts[1]}"
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Get the date portion of the dateTime as YYYY-MM-DD format
     */
    fun getDateDisplay(): String {
        return dateTime.split("T").firstOrNull() ?: ""
    }

    /**
     * Check if this entry is from today
     */
    fun isToday(): Boolean {
        val today = java.time.LocalDate.now().toString()
        return getDateDisplay() == today
    }

    /**
     * Check if this entry is from a specific date
     */
    fun isFromDate(date: String): Boolean {
        return getDateDisplay() == date
    }
}