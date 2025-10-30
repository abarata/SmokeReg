# Project Structure Documentation

## Overview

This document provides a complete map of the SmokeReg project structure, explaining the purpose of each directory and file.

---

## Root Directory

```
smokereg/
├── _documentation/          # Project documentation
├── app/                     # Android application module
├── gradle/                  # Gradle wrapper
├── build.gradle.kts         # Root build configuration
├── settings.gradle.kts      # Project settings
├── gradle.properties        # Gradle properties
├── local.properties         # Local SDK configuration
├── gradlew                  # Gradle wrapper script (Unix)
├── gradlew.bat              # Gradle wrapper script (Windows)
├── build_apk.sh            # APK build script
├── .gitignore              # Git ignore rules
└── README.md               # Project README
```

---

## Documentation Directory

```
_documentation/
├── README.md                    # Documentation index
├── ARCHITECTURE.md              # Architecture overview
├── PROJECT_STRUCTURE.md         # This file
├── DATABASE.md                  # Storage implementation
├── DATA_MODELS.md              # Data model specifications
├── UI.md                       # UI documentation
├── NAVIGATION.md               # Navigation details
├── SETUP.md                    # Development setup
├── BUILD_DEPLOY.md             # Build and deployment
├── TESTING.md                  # Testing guide
├── VIEWMODELS.md               # ViewModel details
├── REPOSITORY.md               # Repository documentation
├── DEPENDENCIES.md             # Dependencies list
├── FUTURE_FEATURES.md          # Roadmap
├── API_REFERENCE.md            # Internal API reference
└── TROUBLESHOOTING.md          # Common issues
```

**Purpose:** Comprehensive documentation for maintainers and developers

---

## App Module Structure

```
app/
├── build.gradle.kts             # App module build config
├── proguard-rules.pro          # ProGuard rules
├── src/                        # Source code
└── .gitignore                  # App-specific ignores
```

---

## Source Code Structure

### Main Source Directory

```
app/src/main/
├── AndroidManifest.xml         # App manifest
├── java/com/smokereg/          # Kotlin source code
└── res/                        # Android resources
```

### Kotlin Source Code

```
app/src/main/java/com/smokereg/
├── MainActivity.kt              # Main activity
├── SmokeRegApplication.kt       # Application class
├── data/                        # Data layer
│   ├── JsonStorageManager.kt    # JSON file operations
│   └── SmokeRepository.kt       # Repository pattern
├── model/                       # Data models
│   └── SmokeEntry.kt            # Core data model
├── navigation/                  # Navigation layer
│   ├── NavigationGraph.kt       # Nav graph setup
│   └── Screen.kt                # Screen routes
├── ui/                         # UI layer
│   ├── components/              # Reusable components
│   │   ├── BottomNavBar.kt
│   │   ├── SmokeEntryCard.kt
│   │   └── StatCard.kt
│   ├── screens/                 # Full screens
│   │   ├── DashboardScreen.kt
│   │   ├── EditEntryDialog.kt
│   │   ├── MainScreen.kt
│   │   └── TimeAdjustDialog.kt
│   └── theme/                   # Material3 theme
│       ├── Theme.kt
│       └── Typography.kt
└── viewmodel/                   # ViewModels
    ├── DashboardViewModel.kt
    └── SmokeViewModel.kt
```

---

## Detailed File Descriptions

### Root Files

#### `MainActivity.kt`
```
Location: app/src/main/java/com/smokereg/MainActivity.kt
Purpose: Single activity that hosts all composables
Key Responsibilities:
  - Set up Jetpack Compose
  - Configure theme
  - Initialize navigation
```

#### `SmokeRegApplication.kt`
```
Location: app/src/main/java/com/smokereg/SmokeRegApplication.kt
Purpose: Application class for dependency management
Key Responsibilities:
  - Initialize dependencies
  - Provide ViewModelFactories
  - Manage application-level state
```

---

### Data Layer (`app/src/main/java/com/smokereg/data/`)

#### `JsonStorageManager.kt`
```
Purpose: Handle all file I/O operations
Responsibilities:
  - Read entries from JSON file
  - Write entries to JSON file
  - CRUD operations
  - Error handling
Lines of Code: ~150
Dependencies: Gson, Kotlin Coroutines
```

#### `SmokeRepository.kt`
```
Purpose: Business logic and data operations
Responsibilities:
  - Provide clean API for data access
  - Filter and query operations
  - Calculate statistics
  - Emit data via StateFlow
Lines of Code: ~200
Dependencies: JsonStorageManager, Coroutines
```

---

### Model Layer (`app/src/main/java/com/smokereg/model/`)

#### `SmokeEntry.kt`
```
Purpose: Core data model
Properties:
  - id: String (UUID)
  - dateTime: String (ISO 8601)
  - isAvoidable: Boolean
Methods:
  - getTimeDisplay()
  - getDateDisplay()
  - isToday()
  - isFromDate()
Lines of Code: ~60
```

---

### Navigation Layer (`app/src/main/java/com/smokereg/navigation/`)

#### `NavigationGraph.kt`
```
Purpose: Set up Compose navigation
Responsibilities:
  - Define navigation routes
  - Handle screen transitions
  - Manage bottom nav bar
Lines of Code: ~50
Dependencies: Jetpack Navigation Compose
```

#### `Screen.kt`
```
Purpose: Define screen routes
Contains:
  - Sealed class for type-safe routes
  - Route string definitions
Lines of Code: ~10
```

---

### UI Layer

#### Components (`app/src/main/java/com/smokereg/ui/components/`)

##### `SmokeEntryCard.kt`
```
Purpose: Display single entry in list
Properties:
  - Shows time
  - Avoidable indicator
  - Edit button
Lines of Code: ~60
```

##### `StatCard.kt`
```
Purpose: Display statistics for period
Properties:
  - Period title
  - Total count
  - Avoidable count
  - Percentage
Lines of Code: ~80
```

##### `BottomNavBar.kt`
```
Purpose: Bottom navigation bar
Items:
  - Home (MainScreen)
  - Dashboard (DashboardScreen)
Lines of Code: ~60
```

#### Screens (`app/src/main/java/com/smokereg/ui/screens/`)

##### `MainScreen.kt`
```
Purpose: Main registration screen
Features:
  - Registration card
  - Avoidable checkbox
  - Quick register button
  - Time adjust button
  - Today's entries list
Lines of Code: ~180
Dependencies: SmokeViewModel
```

##### `DashboardScreen.kt`
```
Purpose: Statistics dashboard
Features:
  - Today stats
  - Yesterday stats
  - Week/Month/Year stats
  - Insights card
  - Refresh button
Lines of Code: ~150
Dependencies: DashboardViewModel
```

##### `EditEntryDialog.kt`
```
Purpose: Edit existing entry
Features:
  - Date/time picker
  - Avoidable toggle
  - Delete button
  - Save/Cancel buttons
Lines of Code: ~120
```

##### `TimeAdjustDialog.kt`
```
Purpose: Custom time selection
Features:
  - Date picker
  - Time picker
  - Avoidable checkbox
  - Register button
Lines of Code: ~130
```

#### Theme (`app/src/main/java/com/smokereg/ui/theme/`)

##### `Theme.kt`
```
Purpose: Material3 theme definition
Contains:
  - Light color scheme
  - Dark color scheme
  - Theme composable
Lines of Code: ~120
```

##### `Typography.kt`
```
Purpose: Typography system
Contains:
  - Material3 typography scale
  - Font definitions
Lines of Code: ~100
```

---

### ViewModel Layer (`app/src/main/java/com/smokereg/viewmodel/`)

#### `SmokeViewModel.kt`
```
Purpose: Manage main screen state
State:
  - UI state (loading, error, checkbox)
  - Today's entries
  - Dialog visibility
Operations:
  - Register smoke
  - Edit entry
  - Delete entry
  - Show/hide dialogs
Lines of Code: ~160
Dependencies: SmokeRepository
```

#### `DashboardViewModel.kt`
```
Purpose: Manage dashboard statistics
State:
  - All period statistics
  - Loading state
Operations:
  - Load statistics
  - Refresh data
  - Calculate metrics
Lines of Code: ~120
Dependencies: SmokeRepository
```

---

## Resources Directory

```
app/src/main/res/
├── values/
│   ├── strings.xml              # String resources
│   ├── colors.xml               # Color definitions
│   └── themes.xml               # App themes
├── xml/
│   ├── backup_rules.xml         # Backup configuration
│   └── data_extraction_rules.xml # Data extraction rules
├── drawable/
│   └── ic_launcher_foreground.xml # Launcher icon foreground
├── mipmap-anydpi-v26/
│   ├── ic_launcher.xml          # Adaptive icon
│   └── ic_launcher_round.xml    # Round adaptive icon
└── mipmap-[density]/            # Icon densities
    ├── ic_launcher.png
    └── ic_launcher_round.png
```

### Resource Files

#### `strings.xml`
```
Location: app/src/main/res/values/strings.xml
Purpose: All user-facing strings
Count: ~25 strings
Includes:
  - Screen titles
  - Button labels
  - Messages
  - Error text
```

#### `colors.xml`
```
Location: app/src/main/res/values/colors.xml
Purpose: Color definitions
Count: ~20 colors
Includes:
  - Primary colors
  - Secondary colors
  - Status colors
  - Text colors
```

#### `themes.xml`
```
Location: app/src/main/res/values/themes.xml
Purpose: App theme definition
Defines:
  - Material theme
  - Status bar color
```

---

## Test Directory

```
app/src/test/java/com/smokereg/
├── model/
│   └── SmokeEntryTest.kt        # Model tests
├── data/
│   └── SmokeRepositoryTest.kt   # Repository tests
└── viewmodel/
    ├── SmokeViewModelTest.kt    # ViewModel tests
    └── DashboardViewModelTest.kt # Dashboard tests
```

### Test Files

#### `SmokeEntryTest.kt`
```
Purpose: Test SmokeEntry model
Tests: ~8 test methods
Coverage:
  - Time display formatting
  - Date extraction
  - Date matching
  - isToday() logic
```

#### `SmokeRepositoryTest.kt`
```
Purpose: Test repository operations
Tests: ~10 test methods
Coverage:
  - CRUD operations
  - Filtering logic
  - Statistics calculation
  - Error handling
```

#### `SmokeViewModelTest.kt`
```
Purpose: Test main ViewModel
Tests: ~12 test methods
Coverage:
  - State management
  - User actions
  - Repository interaction
  - Toast messages
```

#### `DashboardViewModelTest.kt`
```
Purpose: Test dashboard ViewModel
Tests: ~8 test methods
Coverage:
  - Statistics loading
  - State updates
  - Formatted output
```

---

## Build Configuration

### Root Build File
```
File: build.gradle.kts
Purpose: Root project configuration
Defines:
  - Plugin versions
  - Kotlin version
  - Android Gradle Plugin version
```

### App Build File
```
File: app/build.gradle.kts
Purpose: App module configuration
Defines:
  - Compile/target SDK versions
  - Dependencies
  - Build types
  - Compose configuration
Lines: ~100
```

### Gradle Properties
```
File: gradle.properties
Purpose: Gradle configuration
Settings:
  - JVM arguments
  - Parallel build
  - AndroidX
  - Kotlin code style
```

---

## File Size Estimates

### Source Code
```
Total Kotlin Files: ~20
Total Lines of Code: ~2,500
Average File Size: ~125 lines

Breakdown:
- ViewModels: ~280 lines
- Screens: ~580 lines
- Components: ~200 lines
- Data Layer: ~350 lines
- Models: ~60 lines
- Navigation: ~60 lines
- Theme: ~220 lines
- Application/Activity: ~120 lines
- Tests: ~600 lines
```

### Resource Files
```
XML Files: ~10
Total Lines: ~400
```

---

## Code Organization Principles

### Package Structure
```
By Layer (Current):
  ✅ Clear separation
  ✅ Easy to navigate
  ✅ Scales well

Alternative (Not Used):
  By Feature:
    - registration/
    - dashboard/
    - statistics/
```

### Naming Conventions

#### Files
- Activities: `*Activity.kt`
- Composables: Descriptive names (e.g., `MainScreen.kt`)
- ViewModels: `*ViewModel.kt`
- Repositories: `*Repository.kt`
- Models: Descriptive names (e.g., `SmokeEntry.kt`)

#### Classes
- PascalCase for classes
- camelCase for properties/methods
- UPPERCASE for constants

---

## Future Structure Changes

See [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) for planned refactoring:

### Potential Modularization
```
app/
├── feature-registration/
├── feature-dashboard/
├── core-ui/
├── core-data/
└── core-model/
```

---

## Navigation Through Project

### Finding Features
```
Want to find:
  ├─ Registration logic? → ui/screens/MainScreen.kt
  ├─ Statistics? → ui/screens/DashboardScreen.kt
  ├─ Data storage? → data/JsonStorageManager.kt
  ├─ Business logic? → data/SmokeRepository.kt
  ├─ State management? → viewmodel/
  └─ Data models? → model/
```

---

## Related Documentation

- [ARCHITECTURE.md](./ARCHITECTURE.md) - Architecture details
- [SETUP.md](./SETUP.md) - Development setup
- [DATABASE.md](./DATABASE.md) - Storage details
- [UI.md](./UI.md) - UI components