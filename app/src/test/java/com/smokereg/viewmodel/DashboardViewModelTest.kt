package com.smokereg.viewmodel

import com.smokereg.data.SmokeRepository
import com.smokereg.model.SmokeEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.time.LocalDate

/**
 * Unit tests for DashboardViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private lateinit var repository: SmokeRepository
    private lateinit var viewModel: DashboardViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()

        // Setup empty initial state
        whenever(repository.entriesFlow).thenReturn(MutableStateFlow(emptyList()))
        whenever(repository.getTodaysEntries(any())).thenReturn(emptyList())
        whenever(repository.getYesterdaysEntries(any())).thenReturn(emptyList())
        whenever(repository.getThisWeeksEntries(any())).thenReturn(emptyList())
        whenever(repository.getThisMonthsEntries(any())).thenReturn(emptyList())
        whenever(repository.getThisYearsEntries(any())).thenReturn(emptyList())
        whenever(repository.calculateStats(any())).thenReturn(
            SmokeRepository.Stats(total = 0, avoidable = 0)
        )

        viewModel = DashboardViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has zero values`() = runTest {
        val state = viewModel.dashboardState.value

        assertEquals(0, state.todayTotal)
        assertEquals(0, state.todayAvoidable)
        assertEquals(0, state.yesterdayTotal)
        assertEquals(0, state.yesterdayAvoidable)
        assertEquals(0, state.weekTotal)
        assertEquals(0, state.weekAvoidable)
        assertEquals(0, state.monthTotal)
        assertEquals(0, state.monthAvoidable)
        assertEquals(0, state.yearTotal)
        assertEquals(0, state.yearAvoidable)
        assertFalse(state.isLoading)
    }

    @Test
    fun `statistics update when entries change`() = runTest {
        val today = LocalDate.now().toString()
        val todayEntries = listOf(
            SmokeEntry(id = "1", dateTime = "${today}T14:00:00", isAvoidable = true),
            SmokeEntry(id = "2", dateTime = "${today}T15:00:00", isAvoidable = false),
            SmokeEntry(id = "3", dateTime = "${today}T16:00:00", isAvoidable = true)
        )

        whenever(repository.getTodaysEntries(todayEntries)).thenReturn(todayEntries)
        whenever(repository.calculateStats(todayEntries)).thenReturn(
            SmokeRepository.Stats(total = 3, avoidable = 2)
        )

        val entriesFlow = MutableStateFlow(todayEntries)
        whenever(repository.entriesFlow).thenReturn(entriesFlow)

        val newViewModel = DashboardViewModel(repository)
        advanceUntilIdle()

        val state = newViewModel.dashboardState.value
        assertEquals(3, state.todayTotal)
        assertEquals(2, state.todayAvoidable)
    }

    @Test
    fun `refreshStatistics calls repository loadEntries`() = runTest {
        viewModel.refreshStatistics()
        advanceUntilIdle()

        verify(repository).loadEntries()
    }

    @Test
    fun `refreshStatistics sets loading state`() = runTest {
        viewModel.refreshStatistics()

        assertTrue(viewModel.dashboardState.value.isLoading)
    }

    @Test
    fun `getFormattedStats returns correct format with avoidable`() {
        val state = DashboardViewModel.DashboardState()

        val formatted = state.getFormattedStats(total = 5, avoidable = 2)
        assertEquals("5 total (2 avoidable)", formatted)
    }

    @Test
    fun `getFormattedStats returns correct format without avoidable`() {
        val state = DashboardViewModel.DashboardState()

        val formatted = state.getFormattedStats(total = 3, avoidable = 0)
        assertEquals("3 total", formatted)
    }

    @Test
    fun `formatted properties return correct strings`() {
        val state = DashboardViewModel.DashboardState(
            todayTotal = 5,
            todayAvoidable = 2,
            yesterdayTotal = 3,
            yesterdayAvoidable = 0,
            weekTotal = 15,
            weekAvoidable = 7,
            monthTotal = 50,
            monthAvoidable = 20,
            yearTotal = 365,
            yearAvoidable = 100
        )

        assertEquals("5 total (2 avoidable)", state.todayFormatted)
        assertEquals("3 total", state.yesterdayFormatted)
        assertEquals("15 total (7 avoidable)", state.weekFormatted)
        assertEquals("50 total (20 avoidable)", state.monthFormatted)
        assertEquals("365 total (100 avoidable)", state.yearFormatted)
    }

    @Test
    fun `multiple period statistics calculated correctly`() = runTest {
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        val entries = listOf(
            // Today entries
            SmokeEntry(id = "1", dateTime = "${today}T14:00:00", isAvoidable = true),
            SmokeEntry(id = "2", dateTime = "${today}T15:00:00", isAvoidable = false),
            // Yesterday entries
            SmokeEntry(id = "3", dateTime = "${yesterday}T14:00:00", isAvoidable = true),
            SmokeEntry(id = "4", dateTime = "${yesterday}T15:00:00", isAvoidable = true),
            SmokeEntry(id = "5", dateTime = "${yesterday}T16:00:00", isAvoidable = false)
        )

        val todayFiltered = entries.filter { it.getDateDisplay() == today }
        val yesterdayFiltered = entries.filter { it.getDateDisplay() == yesterday }

        whenever(repository.getTodaysEntries(entries)).thenReturn(todayFiltered)
        whenever(repository.getYesterdaysEntries(entries)).thenReturn(yesterdayFiltered)
        whenever(repository.getThisWeeksEntries(entries)).thenReturn(entries)
        whenever(repository.getThisMonthsEntries(entries)).thenReturn(entries)
        whenever(repository.getThisYearsEntries(entries)).thenReturn(entries)

        whenever(repository.calculateStats(todayFiltered)).thenReturn(
            SmokeRepository.Stats(total = 2, avoidable = 1)
        )
        whenever(repository.calculateStats(yesterdayFiltered)).thenReturn(
            SmokeRepository.Stats(total = 3, avoidable = 2)
        )
        whenever(repository.calculateStats(entries)).thenReturn(
            SmokeRepository.Stats(total = 5, avoidable = 3)
        )

        val entriesFlow = MutableStateFlow(entries)
        whenever(repository.entriesFlow).thenReturn(entriesFlow)

        val newViewModel = DashboardViewModel(repository)
        advanceUntilIdle()

        val state = newViewModel.dashboardState.value

        assertEquals(2, state.todayTotal)
        assertEquals(1, state.todayAvoidable)
        assertEquals(3, state.yesterdayTotal)
        assertEquals(2, state.yesterdayAvoidable)
        assertEquals(5, state.weekTotal)
        assertEquals(3, state.weekAvoidable)
    }
}