package com.smokereg.data

import com.smokereg.model.SmokeEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

/**
 * Repository for managing smoke entries
 * Provides a single source of truth for data operations
 */
class SmokeRepository(private val storageManager: JsonStorageManager) {

    private val _entriesFlow = MutableStateFlow<List<SmokeEntry>>(emptyList())
    val entriesFlow: Flow<List<SmokeEntry>> = _entriesFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: Flow<String?> = _error.asStateFlow()

    /**
     * Load all entries from storage
     */
    suspend fun loadEntries() {
        _isLoading.value = true
        _error.value = null

        try {
            val entries = storageManager.readEntries()
                .sortedByDescending { it.dateTime }
            _entriesFlow.value = entries
        } catch (e: Exception) {
            _error.value = "Failed to load entries: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    /**
     * Add a new smoke entry with current timestamp
     */
    suspend fun addEntry(isAvoidable: Boolean): Boolean {
        val entry = SmokeEntry(
            dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            isAvoidable = isAvoidable
        )
        return addEntry(entry)
    }

    /**
     * Add a new smoke entry with custom timestamp
     */
    suspend fun addEntry(entry: SmokeEntry): Boolean {
        _error.value = null

        return try {
            val success = storageManager.addEntry(entry)
            if (success) {
                loadEntries() // Reload to update the flow
            } else {
                _error.value = "Failed to add entry"
            }
            success
        } catch (e: Exception) {
            _error.value = "Error adding entry: ${e.message}"
            false
        }
    }

    /**
     * Update an existing entry
     */
    suspend fun updateEntry(entry: SmokeEntry): Boolean {
        _error.value = null

        return try {
            val success = storageManager.updateEntry(entry)
            if (success) {
                loadEntries() // Reload to update the flow
            } else {
                _error.value = "Failed to update entry"
            }
            success
        } catch (e: Exception) {
            _error.value = "Error updating entry: ${e.message}"
            false
        }
    }

    /**
     * Delete an entry by ID
     */
    suspend fun deleteEntry(entryId: String): Boolean {
        _error.value = null

        return try {
            val success = storageManager.deleteEntry(entryId)
            if (success) {
                loadEntries() // Reload to update the flow
            } else {
                _error.value = "Failed to delete entry"
            }
            success
        } catch (e: Exception) {
            _error.value = "Error deleting entry: ${e.message}"
            false
        }
    }

    /**
     * Get entries for today
     */
    fun getTodaysEntries(entries: List<SmokeEntry>): List<SmokeEntry> {
        val today = LocalDate.now().toString()
        return entries.filter { it.getDateDisplay() == today }
    }

    /**
     * Get entries for yesterday
     */
    fun getYesterdaysEntries(entries: List<SmokeEntry>): List<SmokeEntry> {
        val yesterday = LocalDate.now().minusDays(1).toString()
        return entries.filter { it.getDateDisplay() == yesterday }
    }

    /**
     * Get entries for current week
     */
    fun getThisWeeksEntries(entries: List<SmokeEntry>): List<SmokeEntry> {
        val now = LocalDate.now()
        val weekFields = WeekFields.of(Locale.getDefault())
        val startOfWeek = now.with(TemporalAdjusters.previousOrSame(weekFields.firstDayOfWeek))

        return entries.filter { entry ->
            try {
                val entryDate = LocalDate.parse(entry.getDateDisplay())
                !entryDate.isBefore(startOfWeek) && !entryDate.isAfter(now)
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Get entries for current month
     */
    fun getThisMonthsEntries(entries: List<SmokeEntry>): List<SmokeEntry> {
        val now = LocalDate.now()
        val startOfMonth = now.withDayOfMonth(1)

        return entries.filter { entry ->
            try {
                val entryDate = LocalDate.parse(entry.getDateDisplay())
                !entryDate.isBefore(startOfMonth) && !entryDate.isAfter(now)
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Get entries for current year
     */
    fun getThisYearsEntries(entries: List<SmokeEntry>): List<SmokeEntry> {
        val now = LocalDate.now()
        val startOfYear = now.withDayOfYear(1)

        return entries.filter { entry ->
            try {
                val entryDate = LocalDate.parse(entry.getDateDisplay())
                !entryDate.isBefore(startOfYear) && !entryDate.isAfter(now)
            } catch (e: Exception) {
                false
            }
        }
    }

    /**
     * Calculate statistics for a list of entries
     */
    fun calculateStats(entries: List<SmokeEntry>): Stats {
        val total = entries.size
        val avoidable = entries.count { it.isAvoidable }
        return Stats(total, avoidable)
    }

    /**
     * Data class for statistics
     */
    data class Stats(
        val total: Int,
        val avoidable: Int
    ) {
        val unavoidable: Int = total - avoidable
    }
}