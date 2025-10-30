# Changelog

## Version 1.1 (2025-10-30)

### 🎯 New Features

#### Date Navigation (MainScreen.kt)
**Location:** `app/src/main/java/com/smokereg/ui/screens/MainScreen.kt`

Added complete date selector UI to view and edit entries from any day:
- Previous day button (◀) - Navigate to previous day
- Calendar button with date display - Open date picker dialog
- Today button (🏠) - Jump back to today (only shows when viewing other dates)
- Next day button (▶) - Navigate to next day (only shows when not viewing future)

**Implementation Details:**
- Added `selectedDate` StateFlow in SmokeViewModel
- Added methods: `goToPreviousDay()`, `goToNextDay()`, `goToToday()`, `setSelectedDate()`
- Updated entries filter to use selected date instead of hardcoded "today"
- Used Quad combinator to merge 4 StateFlows in ViewModel init

**Files Modified:**
- `app/src/main/java/com/smokereg/viewmodel/SmokeViewModel.kt` (added date selection logic)
- `app/src/main/java/com/smokereg/ui/screens/MainScreen.kt` (added date selector UI)

---

#### Custom App Icon
**Location:** `app/src/main/res/drawable/ic_launcher_foreground.xml`

Created custom vector drawable with lit cigar design:
- Brown cigar body with texture lines
- Glowing red/orange lit end with ember
- Wisps of smoke
- Gold band detail
- Cut end detail
- Pastel red background color (#FFCCCB)

**Files Modified:**
- `app/src/main/res/drawable/ic_launcher_foreground.xml` (complete rewrite)
- `app/src/main/res/values/colors.xml` (added `ic_launcher_background` color)
- `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` (updated to reference new background)

---

### 🐛 Bug Fixes

#### Dashboard Refresh Infinite Loading
**Location:** `app/src/main/java/com/smokereg/viewmodel/DashboardViewModel.kt:79-92`

**Problem:**
Dashboard refresh button would show loading spinner infinitely and never stop.

**Root Cause:**
StateFlow only emits when the value actually changes. When clicking refresh, if the data hadn't changed, the flow wouldn't emit, and the collector would never receive an emission to reset the `isLoading` state back to `false`.

**Solution:**
```kotlin
fun refreshStatistics() {
    viewModelScope.launch {
        _dashboardState.value = _dashboardState.value.copy(isLoading = true)

        // Load entries from repository
        repository.loadEntries()

        // Force immediate recalculation with current data
        // Use .first() to get the current value immediately
        val currentEntries = repository.entriesFlow.first()
        calculateAndUpdateStats(currentEntries)  // Sets isLoading = false
    }
}
```

**Key Changes:**
- Added `import kotlinx.coroutines.flow.first`
- Use `.first()` to get current flow value immediately without waiting for emission
- Ensures loading state is always reset, even if data is unchanged
- Removed reliance on collector to reset loading state

**Files Modified:**
- `app/src/main/java/com/smokereg/viewmodel/DashboardViewModel.kt`

---

#### List Ordering
**Location:** `app/src/main/java/com/smokereg/data/JsonStorageManager.kt`

**Problem:**
After adding or editing entries, the list wasn't properly ordered by time.

**Solution:**
```kotlin
suspend fun addEntry(entry: SmokeEntry): Boolean {
    val currentEntries = readEntries().toMutableList()
    currentEntries.add(entry)
    // Sort by dateTime descending (most recent first)
    val sortedEntries = currentEntries.sortedByDescending { it.dateTime }
    return writeEntries(sortedEntries)
}
```

**Files Modified:**
- `app/src/main/java/com/smokereg/data/JsonStorageManager.kt`

---

### 🔧 Technical Updates

#### SDK Version Update
**Updated from SDK 34 to SDK 36:**
- `compileSdk = 36`
- `targetSdk = 36`
- Android 15 compatibility

**Files Modified:**
- `app/build.gradle.kts`

#### Build Configuration
**Location:** `local.properties`
- SDK path configured for WSL: `/mnt/c/Users/abara/AppData/Local/Android/Sdk`
- Note: Android Studio may change this to Windows format, but WSL path works for command-line builds

---

### 📚 Documentation Updates

**Files Updated:**
1. **README.md** (project root)
   - Added new features section
   - Added date navigation usage instructions
   - Added version history
   - Added comprehensive documentation references
   - Updated SDK version information

2. **_documentation/README.md**
   - Updated SDK versions
   - Added recent updates section
   - Updated feature list
   - Updated version to 1.1

3. **_documentation/UI.md**
   - Added date selector card documentation
   - Updated MainScreen layout diagram
   - Updated components description

4. **_documentation/TROUBLESHOOTING.md**
   - Added "Dashboard Refresh Loading Infinitely" issue
   - Documented root cause and solution
   - Added code examples and file references

5. **_documentation/CHANGES.md** (new)
   - This file - comprehensive changelog

---

## Important File Locations

### Key Implementation Files
```
app/src/main/java/com/smokereg/
├── viewmodel/
│   ├── SmokeViewModel.kt              (date navigation logic)
│   └── DashboardViewModel.kt          (refresh fix)
├── ui/screens/
│   └── MainScreen.kt                  (date selector UI)
├── data/
│   └── JsonStorageManager.kt          (list ordering)
└── res/
    ├── drawable/
    │   └── ic_launcher_foreground.xml (custom icon)
    └── values/
        └── colors.xml                 (icon background color)
```

### Documentation Files
```
_documentation/
├── README.md                          (index)
├── ARCHITECTURE.md
├── UI.md                             (updated)
├── TROUBLESHOOTING.md                (updated)
├── CHANGES.md                        (this file)
└── [other docs...]
```

---

## Testing Checklist

After these changes, verify:
- ✅ Date navigation works (previous, next, today buttons)
- ✅ Calendar picker opens and selects dates correctly
- ✅ Entries display for selected date
- ✅ Dashboard refresh button stops loading after refresh
- ✅ List entries ordered by time (most recent first)
- ✅ App icon displays correctly on device
- ✅ All existing functionality still works

---

## Build Information

**Build Command:** `./gradlew assembleDebug`
**Output APK:** `app/build/outputs/apk/debug/app-debug.apk`
**APK Size:** ~16 MB
**Build Status:** Successful

---

## Known Issues

None at this time. All reported issues have been resolved.

---

## Future Considerations

See [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) for planned enhancements.

---

**Documentation Last Updated:** 2025-10-30
**Version:** 1.1
**Status:** Production Ready
