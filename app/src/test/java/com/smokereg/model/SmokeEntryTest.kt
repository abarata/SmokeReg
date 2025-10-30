package com.smokereg.model

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for SmokeEntry model
 */
class SmokeEntryTest {

    @Test
    fun `getTimeDisplay returns correct time format`() {
        val entry = SmokeEntry(
            id = "test-id",
            dateTime = "2025-10-28T14:30:00",
            isAvoidable = false
        )
        assertEquals("14:30", entry.getTimeDisplay())
    }

    @Test
    fun `getTimeDisplay handles invalid format`() {
        val entry = SmokeEntry(
            id = "test-id",
            dateTime = "invalid-date",
            isAvoidable = false
        )
        assertEquals("", entry.getTimeDisplay())
    }

    @Test
    fun `getDateDisplay returns correct date format`() {
        val entry = SmokeEntry(
            id = "test-id",
            dateTime = "2025-10-28T14:30:00",
            isAvoidable = false
        )
        assertEquals("2025-10-28", entry.getDateDisplay())
    }

    @Test
    fun `isToday returns true for today's entries`() {
        val today = LocalDate.now().toString()
        val entry = SmokeEntry(
            id = "test-id",
            dateTime = "${today}T14:30:00",
            isAvoidable = false
        )
        assertTrue(entry.isToday())
    }

    @Test
    fun `isToday returns false for past entries`() {
        val entry = SmokeEntry(
            id = "test-id",
            dateTime = "2020-01-01T14:30:00",
            isAvoidable = false
        )
        assertFalse(entry.isToday())
    }

    @Test
    fun `isFromDate returns true for matching date`() {
        val entry = SmokeEntry(
            id = "test-id",
            dateTime = "2025-10-28T14:30:00",
            isAvoidable = false
        )
        assertTrue(entry.isFromDate("2025-10-28"))
    }

    @Test
    fun `isFromDate returns false for non-matching date`() {
        val entry = SmokeEntry(
            id = "test-id",
            dateTime = "2025-10-28T14:30:00",
            isAvoidable = false
        )
        assertFalse(entry.isFromDate("2025-10-29"))
    }

    @Test
    fun `entry with isAvoidable true is created correctly`() {
        val entry = SmokeEntry(
            id = "test-id",
            dateTime = "2025-10-28T14:30:00",
            isAvoidable = true
        )
        assertTrue(entry.isAvoidable)
        assertEquals("test-id", entry.id)
        assertEquals("2025-10-28T14:30:00", entry.dateTime)
    }
}