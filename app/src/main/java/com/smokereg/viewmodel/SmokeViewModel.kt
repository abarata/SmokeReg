package com.smokereg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smokereg.data.SmokeRepository
import com.smokereg.model.SmokeEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ViewModel for the main screen managing smoke entries
 */
class SmokeViewModel(
    private val repository: SmokeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SmokeUiState())
    val uiState: StateFlow<SmokeUiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _todaysEntries = MutableStateFlow<List<SmokeEntry>>(emptyList())
    val todaysEntries: StateFlow<List<SmokeEntry>> = _todaysEntries.asStateFlow()

    private val _showEditDialog = MutableStateFlow<SmokeEntry?>(null)
    val showEditDialog: StateFlow<SmokeEntry?> = _showEditDialog.asStateFlow()

    private val _showTimeAdjustDialog = MutableStateFlow(false)
    val showTimeAdjustDialog: StateFlow<Boolean> = _showTimeAdjustDialog.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    init {
        // Observe repository data and selected date
        viewModelScope.launch {
            combine(
                repository.entriesFlow,
                repository.isLoading,
                repository.error,
                _selectedDate
            ) { entries, isLoading, error, selectedDate ->
                Quad(entries, isLoading, error, selectedDate)
            }.collect { (entries, isLoading, error, selectedDate) ->
                // Filter entries by selected date
                val dateStr = selectedDate.toString()
                _todaysEntries.value = entries.filter { it.getDateDisplay() == dateStr }
                _uiState.value = _uiState.value.copy(
                    isLoading = isLoading,
                    error = error
                )
            }
        }

        // Load initial data
        loadEntries()
    }

    private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

    /**
     * Load entries from repository
     */
    fun loadEntries() {
        viewModelScope.launch {
            repository.loadEntries()
        }
    }

    /**
     * Register a new smoke with current time
     */
    fun registerSmoke(isAvoidable: Boolean) {
        viewModelScope.launch {
            val success = repository.addEntry(isAvoidable)
            if (success) {
                showToast("Smoke registered")
            } else {
                showToast("Failed to register smoke")
            }
        }
    }

    /**
     * Register a smoke with custom date/time
     */
    fun registerSmokeWithCustomTime(
        dateTime: LocalDateTime,
        isAvoidable: Boolean
    ) {
        viewModelScope.launch {
            val entry = SmokeEntry(
                dateTime = dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                isAvoidable = isAvoidable
            )
            val success = repository.addEntry(entry)
            if (success) {
                showToast("Smoke registered")
                _showTimeAdjustDialog.value = false
            } else {
                showToast("Failed to register smoke")
            }
        }
    }

    /**
     * Update the avoidable checkbox state
     */
    fun updateAvoidableCheckbox(checked: Boolean) {
        _uiState.value = _uiState.value.copy(isAvoidableChecked = checked)
    }

    /**
     * Show the edit dialog for an entry
     */
    fun showEditDialog(entry: SmokeEntry) {
        _showEditDialog.value = entry
    }

    /**
     * Hide the edit dialog
     */
    fun hideEditDialog() {
        _showEditDialog.value = null
    }

    /**
     * Show the time adjustment dialog
     */
    fun showTimeAdjustDialog() {
        _showTimeAdjustDialog.value = true
    }

    /**
     * Hide the time adjustment dialog
     */
    fun hideTimeAdjustDialog() {
        _showTimeAdjustDialog.value = false
    }

    /**
     * Update an existing entry
     */
    fun updateEntry(entry: SmokeEntry) {
        viewModelScope.launch {
            val success = repository.updateEntry(entry)
            if (success) {
                showToast("Entry updated")
                hideEditDialog()
            } else {
                showToast("Failed to update entry")
            }
        }
    }

    /**
     * Delete an entry
     */
    fun deleteEntry(entryId: String) {
        viewModelScope.launch {
            val success = repository.deleteEntry(entryId)
            if (success) {
                showToast("Entry deleted")
                hideEditDialog()
            } else {
                showToast("Failed to delete entry")
            }
        }
    }

    /**
     * Show a toast message
     */
    private fun showToast(message: String) {
        _toastMessage.value = message
    }

    /**
     * Clear the toast message
     */
    fun clearToastMessage() {
        _toastMessage.value = null
    }

    /**
     * Change selected date to previous day
     */
    fun goToPreviousDay() {
        _selectedDate.value = _selectedDate.value.minusDays(1)
    }

    /**
     * Change selected date to next day
     */
    fun goToNextDay() {
        _selectedDate.value = _selectedDate.value.plusDays(1)
    }

    /**
     * Set specific date
     */
    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
    }

    /**
     * Go to today
     */
    fun goToToday() {
        _selectedDate.value = LocalDate.now()
    }

    /**
     * UI State for the main screen
     */
    data class SmokeUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val isAvoidableChecked: Boolean = false
    )
}

/**
 * Factory for creating SmokeViewModel with repository
 */
class SmokeViewModelFactory(
    private val repository: SmokeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SmokeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SmokeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}