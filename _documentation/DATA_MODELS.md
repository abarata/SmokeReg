# Data Models Documentation

## Overview

This document details all data models used in the SmokeReg application, their properties, relationships, and usage patterns.

---

## Core Data Model

### SmokeEntry

**File:** `app/src/main/java/com/smokereg/model/SmokeEntry.kt`

**Purpose:** Represents a single smoking event/entry

#### Definition
```kotlin
@Serializable
data class SmokeEntry(
    val id: String = UUID.randomUUID().toString(),
    val dateTime: String,
    val isAvoidable: Boolean = false
)
```

#### Properties

##### `id: String`
- **Type:** String (UUID format)
- **Format:** `"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"`
- **Generation:** Automatic via `UUID.randomUUID()`
- **Purpose:** Unique identifier for each entry
- **Immutable:** Yes (val)
- **Default:** Auto-generated UUID
- **Constraints:**
  - Must be unique across all entries
  - Cannot be null or empty
  - Used for update/delete operations

**Example:**
```kotlin
"550e8400-e29b-41d4-a716-446655440000"
```

##### `dateTime: String`
- **Type:** String (ISO 8601 format)
- **Format:** `"YYYY-MM-DDTHH:mm:ss"`
- **Purpose:** Timestamp of smoking event
- **Immutable:** Yes (val)
- **No Default:** Must be provided
- **Constraints:**
  - Must follow ISO 8601 format
  - Used for sorting and filtering
  - Cannot be null

**Example:**
```kotlin
"2025-10-28T14:30:00"
```

**Components:**
- Date: `2025-10-28` (YYYY-MM-DD)
- Time Separator: `T`
- Time: `14:30:00` (HH:mm:ss)

##### `isAvoidable: Boolean`
- **Type:** Boolean
- **Purpose:** Flag indicating if smoke was unnecessary/avoidable
- **Immutable:** Yes (val)
- **Default:** `false`
- **Constraints:**
  - Only `true` or `false`
  - Used for statistics and analysis

**Example:**
```kotlin
true  // Was avoidable
false // Was not avoidable
```

#### Helper Methods

##### `getTimeDisplay(): String`
```kotlin
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
```

**Purpose:** Extract and format time portion for display
**Returns:** Time in `"HH:mm"` format (e.g., `"14:30"`)
**Error Handling:** Returns empty string on parse failure

**Examples:**
```kotlin
val entry = SmokeEntry(dateTime = "2025-10-28T14:30:00")
entry.getTimeDisplay() // Returns: "14:30"

val invalidEntry = SmokeEntry(dateTime = "invalid")
invalidEntry.getTimeDisplay() // Returns: ""
```

##### `getDateDisplay(): String`
```kotlin
fun getDateDisplay(): String {
    return dateTime.split("T").firstOrNull() ?: ""
}
```

**Purpose:** Extract date portion for filtering
**Returns:** Date in `"YYYY-MM-DD"` format (e.g., `"2025-10-28"`)
**Error Handling:** Returns empty string if parse fails

**Examples:**
```kotlin
val entry = SmokeEntry(dateTime = "2025-10-28T14:30:00")
entry.getDateDisplay() // Returns: "2025-10-28"
```

##### `isToday(): Boolean`
```kotlin
fun isToday(): Boolean {
    val today = java.time.LocalDate.now().toString()
    return getDateDisplay() == today
}
```

**Purpose:** Check if entry is from today
**Returns:** `true` if entry date matches today's date
**Dependency:** Uses `java.time.LocalDate`

**Examples:**
```kotlin
// If today is 2025-10-28
val entry = SmokeEntry(dateTime = "2025-10-28T14:30:00")
entry.isToday() // Returns: true

val oldEntry = SmokeEntry(dateTime = "2025-10-27T14:30:00")
oldEntry.isToday() // Returns: false
```

##### `isFromDate(date: String): Boolean`
```kotlin
fun isFromDate(date: String): Boolean {
    return getDateDisplay() == date
}
```

**Purpose:** Check if entry matches specific date
**Parameters:** `date` - Date string in `"YYYY-MM-DD"` format
**Returns:** `true` if dates match

**Examples:**
```kotlin
val entry = SmokeEntry(dateTime = "2025-10-28T14:30:00")
entry.isFromDate("2025-10-28") // Returns: true
entry.isFromDate("2025-10-27") // Returns: false
```

---

## ViewModel State Models

### SmokeUiState

**File:** `app/src/main/java/com/smokereg/viewmodel/SmokeViewModel.kt`

**Purpose:** Represents UI state for main screen

#### Definition
```kotlin
data class SmokeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAvoidableChecked: Boolean = false
)
```

#### Properties

##### `isLoading: Boolean`
- **Default:** `false`
- **Purpose:** Indicates async operation in progress
- **Usage:** Show/hide loading indicator

##### `error: String?`
- **Default:** `null`
- **Purpose:** Error message to display
- **Usage:** Show error message to user

##### `isAvoidableChecked: Boolean`
- **Default:** `false`
- **Purpose:** State of avoidable checkbox
- **Usage:** Determine if next entry is avoidable

**Example Usage:**
```kotlin
val uiState by viewModel.uiState.collectAsState()

if (uiState.isLoading) {
    CircularProgressIndicator()
}

if (uiState.error != null) {
    Text(text = uiState.error)
}

Checkbox(checked = uiState.isAvoidableChecked)
```

---

### DashboardState

**File:** `app/src/main/java/com/smokereg/viewmodel/DashboardViewModel.kt`

**Purpose:** Represents statistics for dashboard

#### Definition
```kotlin
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
)
```

#### Properties
All integer properties represent count of entries for their respective time period.

#### Helper Method

##### `getFormattedStats(total: Int, avoidable: Int): String`
```kotlin
fun getFormattedStats(total: Int, avoidable: Int): String {
    return if (avoidable > 0) {
        "$total total ($avoidable avoidable)"
    } else {
        "$total total"
    }
}
```

**Purpose:** Format statistics for display
**Returns:** Formatted string like `"5 total (2 avoidable)"` or `"3 total"`

#### Computed Properties
```kotlin
val todayFormatted: String
    get() = getFormattedStats(todayTotal, todayAvoidable)

val yesterdayFormatted: String
    get() = getFormattedStats(yesterdayTotal, yesterdayAvoidable)

// ... similar for week, month, year
```

**Example Usage:**
```kotlin
val state by viewModel.dashboardState.collectAsState()

Text(text = "Today: ${state.todayFormatted}")
// Output: "Today: 5 total (2 avoidable)"
```

---

## Repository Data Models

### Stats

**File:** `app/src/main/java/com/smokereg/data/SmokeRepository.kt`

**Purpose:** Statistical data container

#### Definition
```kotlin
data class Stats(
    val total: Int,
    val avoidable: Int
) {
    val unavoidable: Int = total - avoidable
}
```

#### Properties

##### `total: Int`
- Total number of entries

##### `avoidable: Int`
- Number of avoidable entries

##### `unavoidable: Int` (Computed)
- Number of unavoidable entries
- Calculated as `total - avoidable`

**Example Usage:**
```kotlin
val entries = listOf(
    SmokeEntry(dateTime = "...", isAvoidable = true),
    SmokeEntry(dateTime = "...", isAvoidable = false),
    SmokeEntry(dateTime = "...", isAvoidable = true)
)

val stats = repository.calculateStats(entries)
// stats.total = 3
// stats.avoidable = 2
// stats.unavoidable = 1
```

---

## Data Model Relationships

### Entry Flow
```
User Input
    ↓
SmokeEntry (created)
    ↓
JsonStorageManager (persisted)
    ↓
Repository (managed)
    ↓
ViewModel State (transformed)
    ↓
UI (displayed)
```

### Statistics Flow
```
List<SmokeEntry>
    ↓
Repository.calculateStats()
    ↓
Stats (intermediate)
    ↓
DashboardState (final)
    ↓
UI (displayed)
```

---

## Serialization

### JSON Serialization
Using Gson for JSON serialization:

```kotlin
// Serialize
val entries = listOf(SmokeEntry(...))
val json = gson.toJson(entries)

// Deserialize
val type = object : TypeToken<List<SmokeEntry>>() {}.type
val entries: List<SmokeEntry> = gson.fromJson(json, type)
```

### Kotlinx Serialization
The `@Serializable` annotation enables kotlinx.serialization (currently not actively used but supported):

```kotlin
@Serializable
data class SmokeEntry(...)

// Can be used with:
Json.encodeToString(entry)
Json.decodeFromString<SmokeEntry>(jsonString)
```

---

## Data Validation

### Entry Validation

#### ID Validation
```kotlin
fun isValidId(id: String): Boolean {
    return id.isNotEmpty() &&
           id.matches(Regex("[0-9a-f-]{36}"))
}
```

#### DateTime Validation
```kotlin
fun isValidDateTime(dateTime: String): Boolean {
    return try {
        LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        true
    } catch (e: Exception) {
        false
    }
}
```

#### Complete Validation
```kotlin
fun SmokeEntry.isValid(): Boolean {
    return isValidId(id) && isValidDateTime(dateTime)
}
```

---

## Type Conversions

### String to LocalDateTime
```kotlin
fun String.toLocalDateTime(): LocalDateTime? {
    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    } catch (e: Exception) {
        null
    }
}
```

### LocalDateTime to String
```kotlin
fun LocalDateTime.toIsoString(): String {
    return this.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
}
```

---

## Immutability

### Why Immutable?
All data models use `data class` with `val` properties:

✅ **Benefits:**
- Thread-safe
- Predictable state
- Easy to test
- No accidental modifications
- Works well with StateFlow

### Copying for Updates
```kotlin
val original = SmokeEntry(
    dateTime = "2025-10-28T14:30:00",
    isAvoidable = false
)

val updated = original.copy(isAvoidable = true)
// original remains unchanged
// updated has new isAvoidable value
```

---

## Future Extensions

See [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) for planned model extensions:

### Potential New Fields
```kotlin
data class SmokeEntry(
    val id: String,
    val dateTime: String,
    val isAvoidable: Boolean,
    // Future additions:
    val category: String? = null,  // "social", "stress", etc.
    val notes: String? = null,      // User notes
    val location: String? = null,   // Location context
    val mood: String? = null,       // "happy", "stressed", etc.
    val tags: List<String> = emptyList()  // Custom tags
)
```

### Migration Strategy
When adding fields:
1. Make new fields nullable with defaults
2. Maintain backward compatibility
3. Migrate existing data
4. Update serialization

---

## Testing Data Models

### Unit Tests
**Location:** `app/src/test/java/com/smokereg/model/SmokeEntryTest.kt`

**Example Tests:**
```kotlin
@Test
fun `getTimeDisplay returns correct format`() {
    val entry = SmokeEntry(dateTime = "2025-10-28T14:30:00")
    assertEquals("14:30", entry.getTimeDisplay())
}

@Test
fun `isToday returns true for today's entries`() {
    val today = LocalDate.now().toString()
    val entry = SmokeEntry(dateTime = "${today}T14:30:00")
    assertTrue(entry.isToday())
}
```

---

## Related Documentation

- [DATABASE.md](./DATABASE.md) - Storage details
- [ARCHITECTURE.md](./ARCHITECTURE.md) - Model layer in architecture
- [REPOSITORY.md](./REPOSITORY.md) - Data operations
- [TESTING.md](./TESTING.md) - Testing strategies