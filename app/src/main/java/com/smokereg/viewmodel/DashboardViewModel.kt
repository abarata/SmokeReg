package com.smokereg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.smokereg.data.SmokeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel for the Dashboard screen showing statistics
 */
class DashboardViewModel(
    private val repository: SmokeRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    init {
        loadStatistics()
    }

    /**
     * Load and calculate statistics from repository
     */
    private fun loadStatistics() {
        viewModelScope.launch {
            // Load initial data
            repository.loadEntries()

            // Collect changes from repository
            repository.entriesFlow.collect { entries ->
                calculateAndUpdateStats(entries)
            }
        }
    }

    /**
     * Calculate and update statistics
     */
    private fun calculateAndUpdateStats(entries: List<com.smokereg.model.SmokeEntry>) {
        val todayStats = repository.calculateStats(
            repository.getTodaysEntries(entries)
        )
        val yesterdayStats = repository.calculateStats(
            repository.getYesterdaysEntries(entries)
        )
        val weekStats = repository.calculateStats(
            repository.getThisWeeksEntries(entries)
        )
        val monthStats = repository.calculateStats(
            repository.getThisMonthsEntries(entries)
        )
        val yearStats = repository.calculateStats(
            repository.getThisYearsEntries(entries)
        )

        _dashboardState.value = DashboardState(
            todayTotal = todayStats.total,
            todayAvoidable = todayStats.avoidable,
            yesterdayTotal = yesterdayStats.total,
            yesterdayAvoidable = yesterdayStats.avoidable,
            weekTotal = weekStats.total,
            weekAvoidable = weekStats.avoidable,
            monthTotal = monthStats.total,
            monthAvoidable = monthStats.avoidable,
            yearTotal = yearStats.total,
            yearAvoidable = yearStats.avoidable,
            isLoading = false
        )
    }

    /**
     * Refresh statistics
     */
    fun refreshStatistics() {
        viewModelScope.launch {
            _dashboardState.value = _dashboardState.value.copy(isLoading = true)

            // Load entries from repository
            repository.loadEntries()

            // Force immediate recalculation with current data
            // Use .first() to get the current value immediately
            val currentEntries = repository.entriesFlow.first()
            calculateAndUpdateStats(currentEntries)
        }
    }

    /**
     * State for dashboard statistics
     */
    data class DashboardState(
        val todayTotal: Int = 0,
        val todayAvoidable: Int = 0,
        val yesterdayTotal: Int = 0,
        val yesterdayAvoidable: Int = 0,
        val weekTotal: Int = 0,
        val weekAvoidable: Int = 0,
        val monthTotal: Int = 0,
        val monthAvoidable: Int = 0,
        val yearTotal: Int = 0,
        val yearAvoidable: Int = 0,
        val isLoading: Boolean = false
    ) {
        /**
         * Get formatted string for a period's statistics
         */
        fun getFormattedStats(total: Int, avoidable: Int): String {
            return if (avoidable > 0) {
                "$total total ($avoidable avoidable)"
            } else {
                "$total total"
            }
        }

        val todayFormatted: String get() = getFormattedStats(todayTotal, todayAvoidable)
        val yesterdayFormatted: String get() = getFormattedStats(yesterdayTotal, yesterdayAvoidable)
        val weekFormatted: String get() = getFormattedStats(weekTotal, weekAvoidable)
        val monthFormatted: String get() = getFormattedStats(monthTotal, monthAvoidable)
        val yearFormatted: String get() = getFormattedStats(yearTotal, yearAvoidable)
    }
}

/**
 * Factory for creating DashboardViewModel with repository
 */
class DashboardViewModelFactory(
    private val repository: SmokeRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}