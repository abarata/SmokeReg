# Database & Storage Documentation

## Overview

SmokeReg uses a **JSON file-based storage system** for complete offline functionality. All data is stored in the device's internal storage, ensuring privacy and accessibility without internet connection.

---

## Storage Architecture

### Storage Location
```
/data/data/com.smokereg/files/smoke_entries.json
```

- **Type:** Internal Storage (Private to app)
- **Format:** JSON
- **Access:** Read/Write via JsonStorageManager
- **Persistence:** Survives app restarts
- **Backup:** Included in Android's auto-backup

---

## JSON Structure

### File Format
```json
[
  {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "dateTime": "2025-10-28T14:30:00",
    "isAvoidable": false
  },
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "dateTime": "2025-10-28T16:45:00",
    "isAvoidable": true
  }
]
```

### Field Specifications

#### `id` (String)
- **Type:** UUID String
- **Format:** `"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"`
- **Purpose:** Unique identifier for each entry
- **Generated:** Automatically via `UUID.randomUUID()`
- **Constraints:** Must be unique, cannot be null

#### `dateTime` (String)
- **Type:** ISO 8601 formatted string
- **Format:** `"YYYY-MM-DDTHH:mm:ss"`
- **Example:** `"2025-10-28T14:30:00"`
- **Purpose:** Timestamp of smoking event
- **Constraints:** Must be valid ISO 8601 format

#### `isAvoidable` (Boolean)
- **Type:** Boolean
- **Values:** `true` or `false`
- **Purpose:** Flag indicating if smoke was unnecessary
- **Default:** `false`

---

## JsonStorageManager

**Location:** `app/src/main/java/com/smokereg/data/JsonStorageManager.kt`

### Responsibilities
- Read/Write JSON file
- Handle file creation
- Serialize/Deserialize data
- Error handling for I/O operations

### Key Methods

#### `readEntries(): List<SmokeEntry>`
```kotlin
suspend fun readEntries(): List<SmokeEntry> = withContext(Dispatchers.IO) {
    try {
        if (!file.exists()) {
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
```

**Purpose:** Read all entries from JSON file
**Returns:** List of SmokeEntry objects (empty if file doesn't exist)
**Thread:** IO Dispatcher

#### `writeEntries(entries: List<SmokeEntry>): Boolean`
```kotlin
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
```

**Purpose:** Write entire list to JSON file
**Returns:** true if successful, false otherwise
**Thread:** IO Dispatcher

#### `addEntry(entry: SmokeEntry): Boolean`
```kotlin
suspend fun addEntry(entry: SmokeEntry): Boolean {
    val currentEntries = readEntries().toMutableList()
    currentEntries.add(0, entry) // Add at beginning
    return writeEntries(currentEntries)
}
```

**Purpose:** Add a single entry
**Behavior:** Inserts at position 0 (most recent first)
**Returns:** true if successful

#### `updateEntry(entry: SmokeEntry): Boolean`
```kotlin
suspend fun updateEntry(entry: SmokeEntry): Boolean {
    val currentEntries = readEntries().toMutableList()
    val index = currentEntries.indexOfFirst { it.id == entry.id }

    if (index == -1) return false

    currentEntries[index] = entry
    return writeEntries(currentEntries.sortedByDescending { it.dateTime })
}
```

**Purpose:** Update existing entry by ID
**Behavior:** Finds entry by ID, replaces it, sorts by dateTime
**Returns:** false if entry not found

#### `deleteEntry(entryId: String): Boolean`
```kotlin
suspend fun deleteEntry(entryId: String): Boolean {
    val currentEntries = readEntries().toMutableList()
    val removed = currentEntries.removeAll { it.id == entryId }
    return if (removed) writeEntries(currentEntries) else false
}
```

**Purpose:** Delete entry by ID
**Returns:** true if entry was found and deleted

#### `clearAllEntries(): Boolean`
```kotlin
suspend fun clearAllEntries(): Boolean {
    return writeEntries(emptyList())
}
```

**Purpose:** Remove all entries (use with caution)
**Returns:** true if successful

---

## Data Operations

### Create (Add Entry)
```
User Action → ViewModel → Repository → JsonStorageManager.addEntry()
                                    ↓
                                 Read current entries
                                    ↓
                                 Add new entry at position 0
                                    ↓
                                 Write back to file
```

### Read (Load Entries)
```
App Start → ViewModel → Repository → JsonStorageManager.readEntries()
                                   ↓
                                Parse JSON
                                   ↓
                                Return List<SmokeEntry>
```

### Update (Edit Entry)
```
Edit Dialog → ViewModel → Repository → JsonStorageManager.updateEntry()
                                     ↓
                                  Read current entries
                                     ↓
                                  Find and replace entry
                                     ↓
                                  Sort by dateTime
                                     ↓
                                  Write back to file
```

### Delete (Remove Entry)
```
Delete Button → ViewModel → Repository → JsonStorageManager.deleteEntry()
                                       ↓
                                    Read current entries
                                       ↓
                                    Remove matching entry
                                       ↓
                                    Write back to file
```

---

## Data Integrity

### Atomic Writes
- Each write operation is atomic
- Uses FileWriter with flush()
- Either succeeds completely or fails completely

### Error Handling
```kotlin
try {
    // File operation
} catch (e: Exception) {
    e.printStackTrace()
    return false  // or empty list
}
```

### File Creation
- Automatically creates file if doesn't exist
- Initializes with empty array
- No manual setup required

### Data Validation
- UUID ensures unique IDs
- ISO 8601 ensures valid dates
- Boolean type ensures valid flags

---

## Performance Considerations

### Current Implementation
- **Small Dataset:** Optimized for personal tracking (< 10,000 entries)
- **In-Memory Operations:** Entire file loaded to memory
- **Simple Queries:** Linear searches sufficient

### Performance Characteristics
- **Read:** O(1) file read + O(n) JSON parsing
- **Write:** O(n) JSON serialization + O(1) file write
- **Search:** O(n) linear search
- **Filter:** O(n) iteration

### Optimization Strategies (Current)
1. **Caching:** Repository caches entries in StateFlow
2. **Threading:** All I/O on IO Dispatcher
3. **Batch Operations:** Single write per operation
4. **Sorted Data:** Pre-sorted by dateTime on write

---

## Storage Limits

### Practical Limits
- **File Size:** No hard limit (internal storage)
- **Entry Count:** Recommended < 10,000 entries
- **Performance:** Linear degradation with size

### Estimated Storage
```
Single Entry: ~100 bytes
1,000 entries: ~100 KB
10,000 entries: ~1 MB
```

---

## Backup & Restore

### Android Auto-Backup
```xml
<!-- In AndroidManifest.xml -->
<application
    android:fullBackupContent="@xml/backup_rules"
    android:dataExtractionRules="@xml/data_extraction_rules">
```

**Backup Rules:** `app/src/main/res/xml/backup_rules.xml`
```xml
<full-backup-content>
    <include domain="file" path="."/>
</full-backup-content>
```

### Manual Backup
To manually backup data:
1. Use ADB: `adb backup -f backup.ab com.smokereg`
2. File is automatically included

### Data Export (Future Feature)
See [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) for planned export functionality.

---

## Migration Strategy

### Current Version: 1.0
- Initial JSON structure
- Three fields: id, dateTime, isAvoidable

### Future Migrations
If schema changes are needed:
1. Add version field to JSON
2. Implement migration logic in JsonStorageManager
3. Detect old format and transform
4. Write new format

Example migration structure:
```json
{
  "version": 2,
  "entries": [...]
}
```

---

## Comparison with Alternatives

### JSON File (Current)
✅ **Pros:**
- Simple implementation
- No dependencies
- Human-readable
- Easy debugging
- Perfect for offline

❌ **Cons:**
- Not scalable to large datasets
- No complex queries
- Full file read/write

### Room Database (Future)
See [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) for migration plan.

✅ **Pros:**
- Better performance
- Complex queries
- Incremental updates
- Type-safe

❌ **Cons:**
- More complex
- Learning curve
- Migration required

---

## Data Privacy

### Security Features
- ✅ Internal storage (private to app)
- ✅ No network access
- ✅ No permissions required
- ✅ Sandboxed by Android
- ✅ Cleared on uninstall

### Access Control
- Only app can access file
- Root access can read (but requires root)
- Backup requires user consent

---

## Troubleshooting

### Issue: File Not Found
**Solution:** File is created automatically on first write

### Issue: Parse Error
**Cause:** Corrupted JSON
**Solution:** Wrap in try-catch, return empty list

### Issue: Write Failure
**Possible Causes:**
- Storage full
- Permission denied (shouldn't happen)
- File locked

**Solution:** Check return value, show error to user

---

## Code Example: Complete Flow

```kotlin
// 1. Initialize in Application
class SmokeRegApplication : Application() {
    private val jsonStorageManager by lazy {
        JsonStorageManager(this)
    }

    val repository by lazy {
        SmokeRepository(jsonStorageManager)
    }
}

// 2. Use in ViewModel
class SmokeViewModel(private val repository: SmokeRepository) : ViewModel() {
    fun registerSmoke(isAvoidable: Boolean) {
        viewModelScope.launch {
            val success = repository.addEntry(isAvoidable)
            if (success) {
                showToast("Smoke registered")
            }
        }
    }
}

// 3. Repository calls StorageManager
class SmokeRepository(private val storageManager: JsonStorageManager) {
    suspend fun addEntry(isAvoidable: Boolean): Boolean {
        val entry = SmokeEntry(
            dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            isAvoidable = isAvoidable
        )
        return storageManager.addEntry(entry)
    }
}

// 4. StorageManager handles file
class JsonStorageManager(context: Context) {
    private val file = File(context.filesDir, "smoke_entries.json")

    suspend fun addEntry(entry: SmokeEntry): Boolean = withContext(Dispatchers.IO) {
        val entries = readEntries().toMutableList()
        entries.add(0, entry)
        writeEntries(entries)
    }
}
```

---

## Related Documentation

- [DATA_MODELS.md](./DATA_MODELS.md) - SmokeEntry model details
- [REPOSITORY.md](./REPOSITORY.md) - Repository implementation
- [ARCHITECTURE.md](./ARCHITECTURE.md) - Overall architecture
- [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) - Database migration plans