package com.smokereg.data

import com.smokereg.model.SmokeEntry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import java.time.LocalDate

/**
 * Unit tests for SmokeRepository
 */
class SmokeRepositoryTest {

    private lateinit var storageManager: JsonStorageManager
    private lateinit var repository: SmokeRepository

    @Before
    fun setup() {
        storageManager = mock()
        repository = SmokeRepository(storageManager)
    }

    @Test
    fun `loadEntries updates flow with sorted entries`() = runTest {
        val entries = listOf(
            SmokeEntry(id = "1", dateTime = "2025-10-28T14:00:00", isAvoidable = false),
            SmokeEntry(id = "2", dateTime = "2025-10-28T15:00:00", isAvoidable = true),
            SmokeEntry(id = "3", dateTime = "2025-10-28T13:00:00", isAvoidable = false)
        )
        whenever(storageManager.readEntries()).thenReturn(entries)

        repository.loadEntries()

        val result = repository.entriesFlow.first()
        assertEquals(3, result.size)
        // Should be sorted by dateTime descending
        assertEquals("2", result[0].id)
        assertEquals("1", result[1].id)
        assertEquals("3", result[2].id)
    }

    @Test
    fun `addEntry with boolean creates entry with current time`() = runTest {
        whenever(storageManager.addEntry(any())).thenReturn(true)
        whenever(storageManager.readEntries()).thenReturn(emptyList())

        val success = repository.addEntry(isAvoidable = true)

        assertTrue(success)
        verify(storageManager).addEntry(argThat { isAvoidable })
    }

    @Test
    fun `updateEntry calls storage manager and reloads`() = runTest {
        val entry = SmokeEntry(
            id = "test-id",
            dateTime = "2025-10-28T14:00:00",
            isAvoidable = false
        )
        whenever(storageManager.updateEntry(entry)).thenReturn(true)
        whenever(storageManager.readEntries()).thenReturn(listOf(entry))

        val success = repository.updateEntry(entry)

        assertTrue(success)
        verify(storageManager).updateEntry(entry)
        verify(storageManager, atLeastOnce()).readEntries()
    }

    @Test
    fun `deleteEntry calls storage manager and reloads`() = runTest {
        whenever(storageManager.deleteEntry("test-id")).thenReturn(true)
        whenever(storageManager.readEntries()).thenReturn(emptyList())

        val success = repository.deleteEntry("test-id")

        assertTrue(success)
        verify(storageManager).deleteEntry("test-id")
        verify(storageManager, atLeastOnce()).readEntries()
    }

    @Test
    fun `getTodaysEntries filters correctly`() {
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        val entries = listOf(
            SmokeEntry(id = "1", dateTime = "${today}T14:00:00", isAvoidable = false),
            SmokeEntry(id = "2", dateTime = "${yesterday}T15:00:00", isAvoidable = true),
            SmokeEntry(id = "3", dateTime = "${today}T16:00:00", isAvoidable = false)
        )

        val result = repository.getTodaysEntries(entries)

        assertEquals(2, result.size)
        assertTrue(result.all { it.getDateDisplay() == today })
    }

    @Test
    fun `getYesterdaysEntries filters correctly`() {
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        val entries = listOf(
            SmokeEntry(id = "1", dateTime = "${today}T14:00:00", isAvoidable = false),
            SmokeEntry(id = "2", dateTime = "${yesterday}T15:00:00", isAvoidable = true),
            SmokeEntry(id = "3", dateTime = "${yesterday}T16:00:00", isAvoidable = false)
        )

        val result = repository.getYesterdaysEntries(entries)

        assertEquals(2, result.size)
        assertTrue(result.all { it.getDateDisplay() == yesterday })
    }

    @Test
    fun `calculateStats returns correct values`() {
        val entries = listOf(
            SmokeEntry(id = "1", dateTime = "2025-10-28T14:00:00", isAvoidable = true),
            SmokeEntry(id = "2", dateTime = "2025-10-28T15:00:00", isAvoidable = false),
            SmokeEntry(id = "3", dateTime = "2025-10-28T16:00:00", isAvoidable = true),
            SmokeEntry(id = "4", dateTime = "2025-10-28T17:00:00", isAvoidable = false),
            SmokeEntry(id = "5", dateTime = "2025-10-28T18:00:00", isAvoidable = true)
        )

        val stats = repository.calculateStats(entries)

        assertEquals(5, stats.total)
        assertEquals(3, stats.avoidable)
        assertEquals(2, stats.unavoidable)
    }

    @Test
    fun `error handling when storage fails`() = runTest {
        whenever(storageManager.addEntry(any())).thenReturn(false)
        whenever(storageManager.readEntries()).thenReturn(emptyList())

        val success = repository.addEntry(isAvoidable = false)

        assertFalse(success)
        assertNotNull(repository.error.first())
    }
}