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
import java.time.LocalDateTime

/**
 * Unit tests for SmokeViewModel
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SmokeViewModelTest {

    private lateinit var repository: SmokeRepository
    private lateinit var viewModel: SmokeViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()

        // Mock repository flows
        whenever(repository.entriesFlow).thenReturn(MutableStateFlow(emptyList()))
        whenever(repository.isLoading).thenReturn(MutableStateFlow(false))
        whenever(repository.error).thenReturn(MutableStateFlow(null))

        viewModel = SmokeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has correct default values`() = runTest {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertFalse(state.isAvoidableChecked)
    }

    @Test
    fun `updateAvoidableCheckbox updates state`() = runTest {
        viewModel.updateAvoidableCheckbox(true)
        assertTrue(viewModel.uiState.value.isAvoidableChecked)

        viewModel.updateAvoidableCheckbox(false)
        assertFalse(viewModel.uiState.value.isAvoidableChecked)
    }

    @Test
    fun `registerSmoke calls repository with correct parameter`() = runTest {
        whenever(repository.addEntry(anyBoolean())).thenReturn(true)

        viewModel.registerSmoke(isAvoidable = true)

        verify(repository).addEntry(true)
    }

    @Test
    fun `registerSmoke shows success toast on success`() = runTest {
        whenever(repository.addEntry(anyBoolean())).thenReturn(true)

        viewModel.registerSmoke(isAvoidable = false)
        advanceUntilIdle()

        assertEquals("Smoke registered", viewModel.toastMessage.value)
    }

    @Test
    fun `registerSmoke shows error toast on failure`() = runTest {
        whenever(repository.addEntry(anyBoolean())).thenReturn(false)

        viewModel.registerSmoke(isAvoidable = false)
        advanceUntilIdle()

        assertEquals("Failed to register smoke", viewModel.toastMessage.value)
    }

    @Test
    fun `showEditDialog and hideEditDialog manage dialog state`() {
        val entry = SmokeEntry(
            id = "test",
            dateTime = "2025-10-28T14:00:00",
            isAvoidable = false
        )

        viewModel.showEditDialog(entry)
        assertEquals(entry, viewModel.showEditDialog.value)

        viewModel.hideEditDialog()
        assertNull(viewModel.showEditDialog.value)
    }

    @Test
    fun `showTimeAdjustDialog and hideTimeAdjustDialog manage dialog state`() {
        viewModel.showTimeAdjustDialog()
        assertTrue(viewModel.showTimeAdjustDialog.value)

        viewModel.hideTimeAdjustDialog()
        assertFalse(viewModel.showTimeAdjustDialog.value)
    }

    @Test
    fun `updateEntry calls repository and shows toast`() = runTest {
        val entry = SmokeEntry(
            id = "test",
            dateTime = "2025-10-28T14:00:00",
            isAvoidable = true
        )
        whenever(repository.updateEntry(entry)).thenReturn(true)

        viewModel.updateEntry(entry)
        advanceUntilIdle()

        verify(repository).updateEntry(entry)
        assertEquals("Entry updated", viewModel.toastMessage.value)
    }

    @Test
    fun `deleteEntry calls repository and shows toast`() = runTest {
        whenever(repository.deleteEntry("test-id")).thenReturn(true)

        viewModel.deleteEntry("test-id")
        advanceUntilIdle()

        verify(repository).deleteEntry("test-id")
        assertEquals("Entry deleted", viewModel.toastMessage.value)
    }

    @Test
    fun `clearToastMessage sets message to null`() {
        viewModel.clearToastMessage()
        assertNull(viewModel.toastMessage.value)
    }

    @Test
    fun `todaysEntries updates when repository entries change`() = runTest {
        val todayEntry = SmokeEntry(
            id = "today",
            dateTime = LocalDateTime.now().toString(),
            isAvoidable = false
        )
        val entries = listOf(todayEntry)

        whenever(repository.getTodaysEntries(entries)).thenReturn(entries)

        val entriesFlow = MutableStateFlow(entries)
        whenever(repository.entriesFlow).thenReturn(entriesFlow)
        whenever(repository.isLoading).thenReturn(MutableStateFlow(false))
        whenever(repository.error).thenReturn(MutableStateFlow(null))

        val newViewModel = SmokeViewModel(repository)
        advanceUntilIdle()

        assertEquals(1, newViewModel.todaysEntries.value.size)
    }
}