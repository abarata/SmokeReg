# SmokeReg Architecture Documentation

## Overview

SmokeReg follows the **MVVM (Model-View-ViewModel)** architectural pattern with a **Repository** layer for data operations. The app is built entirely in Kotlin using Jetpack Compose for the UI.

---

## Architectural Layers

### 1. Presentation Layer (UI)
**Location:** `app/src/main/java/com/smokereg/ui/`

Consists of:
- **Screens** - Full-screen composables
- **Components** - Reusable UI elements
- **Theme** - Material3 theming

**Technology:**
- Jetpack Compose
- Material3 Design System
- Compose Navigation

**Key Files:**
```
ui/
├── screens/
│   ├── MainScreen.kt           # Main registration screen
│   ├── DashboardScreen.kt      # Statistics screen
│   ├── EditEntryDialog.kt      # Edit dialog
│   └── TimeAdjustDialog.kt     # Time picker dialog
├── components/
│   ├── BottomNavBar.kt         # Navigation bar
│   ├── SmokeEntryCard.kt       # Entry card component
│   └── StatCard.kt             # Statistics card
└── theme/
    ├── Theme.kt                # App theme definition
    └── Typography.kt           # Typography system
```

### 2. ViewModel Layer
**Location:** `app/src/main/java/com/smokereg/viewmodel/`

Manages UI state and business logic:
- **SmokeViewModel** - Handles main screen logic and entry operations
- **DashboardViewModel** - Manages statistics calculations and display

**Responsibilities:**
- State management
- User interaction handling
- Calling repository methods
- UI state transformation

**Key Files:**
```
viewmodel/
├── SmokeViewModel.kt           # Main screen ViewModel
├── DashboardViewModel.kt       # Dashboard ViewModel
└── [Factory classes]           # ViewModel factories
```

### 3. Repository Layer
**Location:** `app/src/main/java/com/smokereg/data/`

Single source of truth for data operations:
- **SmokeRepository** - Business logic and data operations
- **JsonStorageManager** - File I/O operations

**Responsibilities:**
- CRUD operations
- Data filtering and querying
- Statistics calculations
- Error handling

**Key Files:**
```
data/
├── SmokeRepository.kt          # Repository pattern implementation
└── JsonStorageManager.kt       # JSON file operations
```

### 4. Model Layer
**Location:** `app/src/main/java/com/smokereg/model/`

Data models and entities:
- **SmokeEntry** - Core data model

**Key Files:**
```
model/
└── SmokeEntry.kt               # Data class for smoke entries
```

### 5. Navigation Layer
**Location:** `app/src/main/java/com/smokereg/navigation/`

Handles app navigation:
- **NavigationGraph** - Compose navigation setup
- **Screen** - Route definitions

**Key Files:**
```
navigation/
├── NavigationGraph.kt          # Navigation setup
└── Screen.kt                   # Screen routes
```

---

## Data Flow

### User Action Flow
```
User Interaction
       ↓
   UI Screen (Composable)
       ↓
   ViewModel
       ↓
   Repository
       ↓
   JsonStorageManager
       ↓
   JSON File (Disk)
```

### Data Update Flow
```
JSON File (Disk)
       ↓
   JsonStorageManager
       ↓
   Repository (StateFlow)
       ↓
   ViewModel (State)
       ↓
   UI Screen (Recomposition)
```

---

## Design Patterns

### 1. MVVM Pattern
- **Model:** Data classes and storage logic
- **View:** Composable UI components
- **ViewModel:** State management and business logic

### 2. Repository Pattern
- Abstracts data source
- Provides clean API for data operations
- Handles data transformations

### 3. Factory Pattern
- ViewModelFactory classes for dependency injection
- Ensures proper ViewModel instantiation

### 4. Observer Pattern
- StateFlow for reactive data updates
- UI automatically updates when data changes

---

## Dependency Injection

**Current Implementation:** Manual DI via Application class

```kotlin
SmokeRegApplication
    ↓ provides
JsonStorageManager
    ↓ provides
SmokeRepository
    ↓ provides
[ViewModelFactories]
    ↓ provides
[ViewModels]
```

**Application Class:** `SmokeRegApplication.kt`
- Manages singleton instances
- Provides dependencies to ViewModels
- Ensures single data source

---

## State Management

### ViewModel State
Uses Kotlin `StateFlow` for reactive state management:

```kotlin
// Example from SmokeViewModel
private val _uiState = MutableStateFlow(SmokeUiState())
val uiState: StateFlow<SmokeUiState> = _uiState.asStateFlow()
```

### UI State Collection
```kotlin
// In Composables
val uiState by viewModel.uiState.collectAsState()
```

---

## Threading Model

### Coroutines
All asynchronous operations use Kotlin Coroutines:

- **Main Thread:** UI updates
- **IO Dispatcher:** File operations
- **Default Dispatcher:** Heavy computations

```kotlin
// Example from Repository
suspend fun readEntries(): List<SmokeEntry> = withContext(Dispatchers.IO) {
    // File I/O on IO dispatcher
}
```

---

## Lifecycle Management

### ViewModels
- Survive configuration changes
- Automatically cleared when no longer needed
- Handle coroutine lifecycle

### Composables
- Follow Compose lifecycle
- Automatically recompose on state changes
- Clean up side effects with `DisposableEffect`

---

## Error Handling

### Repository Level
```kotlin
try {
    // Operation
    _error.value = null
} catch (e: Exception) {
    _error.value = "Error message: ${e.message}"
}
```

### ViewModel Level
- Observes error state from Repository
- Displays toast messages to user
- Maintains app stability

---

## Offline-First Architecture

### Key Principles
1. **No Network Required:** All operations work offline
2. **Local Storage:** JSON file as single source of truth
3. **Instant Feedback:** All operations are synchronous from user perspective
4. **Data Persistence:** Survives app restarts

### Storage Strategy
- Single JSON file in internal storage
- Atomic writes to prevent corruption
- Automatic file creation
- Error handling for I/O failures

---

## Scalability Considerations

### Current Architecture Supports
- ✅ Adding new screens
- ✅ Adding new data fields
- ✅ Extending statistics
- ✅ Multiple ViewModels
- ✅ Shared state between screens

### Future Enhancements
- Migration to Room database (see FUTURE_FEATURES.md)
- Hilt/Dagger for DI (see FUTURE_FEATURES.md)
- Multi-module architecture
- Feature modules

---

## Testing Architecture

### Unit Tests
- **Models:** Data class methods
- **Repository:** Data operations and filtering
- **ViewModels:** State management and business logic

### Test Structure
```
src/test/
├── model/
│   └── SmokeEntryTest.kt
├── data/
│   └── SmokeRepositoryTest.kt
└── viewmodel/
    ├── SmokeViewModelTest.kt
    └── DashboardViewModelTest.kt
```

---

## Architecture Benefits

### Separation of Concerns
- Each layer has single responsibility
- Easy to understand and maintain
- Clear boundaries between components

### Testability
- Each layer can be tested independently
- Mock dependencies easily
- High test coverage possible

### Maintainability
- Changes isolated to specific layers
- Clear code organization
- Easy to locate functionality

### Scalability
- Easy to add new features
- Can refactor individual layers
- Supports growth

---

## Related Documentation

- [PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md) - Detailed file organization
- [VIEWMODELS.md](./VIEWMODELS.md) - ViewModel implementations
- [REPOSITORY.md](./REPOSITORY.md) - Repository details
- [UI.md](./UI.md) - UI layer details
- [DATABASE.md](./DATABASE.md) - Storage implementation