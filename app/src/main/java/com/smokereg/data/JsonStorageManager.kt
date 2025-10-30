package com.smokereg.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.smokereg.model.SmokeEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Manages reading and writing smoke entries to/from a JSON file in internal storage
 */
class JsonStorageManager(private val context: Context) {

    companion object {
        private const val FILE_NAME = "smoke_entries.json"
    }

    private val gson: Gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    private val file: File
        get() = File(context.filesDir, FILE_NAME)

    /**
     * Read all smoke entries from the JSON file
     * @return List of smoke entries, or empty list if file doesn't exist or is empty
     */
    suspend fun readEntries(): List<SmokeEntry> = withContext(Dispatchers.IO) {
        try {
            if (!file.exists()) {
                // Create empty file if it doesn't exist
                file.createNewFile()
                return@withContext emptyList()
            }

            if (file.length() == 0L) {
                return@withContext emptyList()
            }

            FileReader(file).use { reader ->
                val type = object : TypeToken<List<SmokeEntry>>() {}.type
                gson.fromJson<List<SmokeEntry>>(reader, type) ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /**
     * Write smoke entries to the JSON file
     * @param entries List of smoke entries to save
     * @return true if successful, false otherwise
     */
    suspend fun writeEntries(entries: List<SmokeEntry>): Boolean = withContext(Dispatchers.IO) {
        try {
            FileWriter(file).use { writer ->
                gson.toJson(entries, writer)
                writer.flush()
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * Add a single entry to the existing entries
     * @param entry The smoke entry to add
     * @return true if successful, false otherwise
     */
    suspend fun addEntry(entry: SmokeEntry): Boolean {
        val currentEntries = readEntries().toMutableList()
        currentEntries.add(entry)
        // Sort by dateTime descending (most recent first)
        val sortedEntries = currentEntries.sortedByDescending { it.dateTime }
        return writeEntries(sortedEntries)
    }

    /**
     * Update an existing entry
     * @param entry The updated smoke entry
     * @return true if successful, false otherwise
     */
    suspend fun updateEntry(entry: SmokeEntry): Boolean {
        val currentEntries = readEntries().toMutableList()
        val index = currentEntries.indexOfFirst { it.id == entry.id }

        if (index == -1) {
            return false
        }

        currentEntries[index] = entry
        return writeEntries(currentEntries.sortedByDescending { it.dateTime })
    }

    /**
     * Delete an entry by ID
     * @param entryId The ID of the entry to delete
     * @return true if successful, false otherwise
     */
    suspend fun deleteEntry(entryId: String): Boolean {
        val currentEntries = readEntries().toMutableList()
        val removed = currentEntries.removeAll { it.id == entryId }

        return if (removed) {
            writeEntries(currentEntries)
        } else {
            false
        }
    }

    /**
     * Clear all entries (use with caution)
     * @return true if successful, false otherwise
     */
    suspend fun clearAllEntries(): Boolean {
        return writeEntries(emptyList())
    }
}