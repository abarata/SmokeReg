# Dependencies Documentation

## Overview

Complete list of all dependencies used in SmokeReg, including versions, purposes, and justifications.

---

## Dependency Summary

**Total Dependencies:** 20+
**Build Tool:** Gradle 8.2
**Kotlin Version:** 1.9.20
**Android Gradle Plugin:** 8.2.0

---

## Core Android Dependencies

### 1. Core KTX
```kotlin
implementation("androidx.core:core-ktx:1.12.0")
```

**Purpose:** Kotlin extensions for Android framework APIs
**Why:** Provides Kotlin-friendly APIs for Android
**Size:** ~1.5 MB
**Required:** Yes

**Features Used:**
- Extension functions
- Lambda support
- Coroutines integration

---

### 2. Lifecycle Runtime KTX
```kotlin
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
```

**Purpose:** Lifecycle-aware components
**Why:** Manage app lifecycle and coroutines
**Size:** ~500 KB
**Required:** Yes

**Features Used:**
- lifecycleScope
- repeatOnLifecycle
- Lifecycle observation

---

### 3. Activity Compose
```kotlin
implementation("androidx.activity:activity-compose:1.8.2")
```

**Purpose:** Compose integration with Activities
**Why:** Bridge between Activity and Compose
**Size:** ~300 KB
**Required:** Yes (for Compose apps)

**Features Used:**
- setContent
- ComponentActivity
- Compose lifecycle

---

## Jetpack Compose Dependencies

### 4. Compose BOM
```kotlin
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
```

**Purpose:** Bill of Materials for Compose versions
**Why:** Ensures compatible Compose library versions
**Size:** N/A (BOM)
**Required:** Yes (for Compose apps)

**Managed Libraries:**
- compose-ui
- compose-material3
- compose-foundation
- compose-runtime

---

### 5. Compose UI
```kotlin
implementation("androidx.compose.ui:ui")
```

**Purpose:** Core Compose UI functionality
**Why:** Essential for building Compose UIs
**Size:** ~2 MB
**Required:** Yes
**Version:** Managed by BOM (1.6.0+)

**Features Used:**
- Composable functions
- Modifiers
- Layout components

---

### 6. Compose UI Graphics
```kotlin
implementation("androidx.compose.ui:ui-graphics")
```

**Purpose:** Graphics and drawing APIs
**Why:** Custom graphics and effects
**Size:** ~500 KB
**Required:** Yes
**Version:** Managed by BOM

**Features Used:**
- Color manipulation
- Graphics context

---

### 7. Compose UI Tooling Preview
```kotlin
implementation("androidx.compose.ui:ui-tooling-preview")
```

**Purpose:** Preview annotations and tools
**Why:** Enable @Preview in Android Studio
**Size:** ~100 KB
**Required:** No (dev-only, but helpful)
**Version:** Managed by BOM

**Features Used:**
- @Preview annotation
- Preview parameters

---

### 8. Material3
```kotlin
implementation("androidx.compose.material3:material3")
```

**Purpose:** Material Design 3 components
**Why:** Modern Material Design UI components
**Size:** ~2 MB
**Required:** Yes
**Version:** Managed by BOM (1.2.0+)

**Features Used:**
- Material3 theme
- Buttons, Cards, etc.
- TopAppBar
- NavigationBar
- Dialogs

---

### 9. Material Icons Extended
```kotlin
implementation("androidx.compose.material:material-icons-extended")
```

**Purpose:** Extended icon set
**Why:** Access to full Material icon library
**Size:** ~5 MB
**Required:** No (but useful)
**Version:** Managed by BOM

**Icons Used:**
- Home, BarChart
- Edit, Delete
- AccessTime, CalendarToday
- Add, Refresh
- Warning

**Alternative:** Could use custom icons to reduce size

---

## Navigation

### 10. Navigation Compose
```kotlin
implementation("androidx.navigation:navigation-compose:2.7.6")
```

**Purpose:** Navigation for Jetpack Compose
**Why:** Handle screen navigation
**Size:** ~800 KB
**Required:** Yes (for multi-screen apps)

**Features Used:**
- NavHost
- NavController
- composable() navigation
- Screen routes

---

## ViewModel & State

### 11. ViewModel Compose
```kotlin
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
```

**Purpose:** ViewModel integration with Compose
**Why:** Create and access ViewModels in Composables
**Size:** ~200 KB
**Required:** Yes (for MVVM)

**Features Used:**
- viewModel() function
- ViewModel lifecycle

---

### 12. Runtime Compose
```kotlin
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
```

**Purpose:** Lifecycle-aware Compose utilities
**Why:** Collect StateFlow in Compose
**Size:** ~100 KB
**Required:** Yes (for StateFlow)

**Features Used:**
- collectAsState()
- collectAsStateWithLifecycle()

---

## Coroutines

### 13. Coroutines Android
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

**Purpose:** Coroutines for Android
**Why:** Asynchronous programming
**Size:** ~600 KB
**Required:** Yes

**Features Used:**
- Dispatchers.Main
- Dispatchers.IO
- viewModelScope
- suspend functions

---

### 14. Coroutines Core
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
```

**Purpose:** Core coroutines library
**Why:** Foundation for coroutines
**Size:** ~1 MB
**Required:** Yes

**Features Used:**
- Flow
- StateFlow
- MutableStateFlow
- launch, async

---

## JSON & Serialization

### 15. Gson
```kotlin
implementation("com.google.code.gson:gson:2.10.1")
```

**Purpose:** JSON serialization/deserialization
**Why:** Handle JSON file storage
**Size:** ~500 KB
**Required:** Yes

**Features Used:**
- toJson()
- fromJson()
- TypeToken
- Pretty printing

**Alternative:** Kotlinx Serialization (already included)

---

### 16. Kotlinx Serialization JSON
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
```

**Purpose:** Kotlin-native JSON serialization
**Why:** Type-safe serialization (future use)
**Size:** ~400 KB
**Required:** No (currently not actively used)

**Note:** Gson is currently primary, but Kotlinx Serialization is set up for future use

---

### 17. Kotlinx DateTime
```kotlin
implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
```

**Purpose:** Date and time handling
**Why:** Multiplatform date/time operations
**Size:** ~200 KB
**Required:** No (using java.time currently)

**Note:** Included for potential future use

---

## System UI

### 18. Accompanist System UI Controller
```kotlin
implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")
```

**Purpose:** Control system UI (status bar, nav bar)
**Why:** Set status bar color from Compose
**Size:** ~100 KB
**Required:** No (nice-to-have)

**Features Used:**
- setStatusBarColor()
- Status bar icon color

**Note:** Part of Accompanist library (Google-maintained Compose utilities)

---

## Testing Dependencies

### 19. JUnit
```kotlin
testImplementation("junit:junit:4.13.2")
```

**Purpose:** Unit testing framework
**Why:** Standard Java/Kotlin testing
**Size:** N/A (test-only)
**Required:** Yes (for testing)

**Features Used:**
- @Test annotation
- Assertions
- Test runners

---

### 20. Coroutines Test
```kotlin
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
```

**Purpose:** Test coroutines and flows
**Why:** Test asynchronous code
**Size:** N/A (test-only)
**Required:** Yes (for testing coroutines)

**Features Used:**
- runTest
- TestDispatcher
- UnconfinedTestDispatcher
- advanceUntilIdle()

---

### 21. Mockito Core
```kotlin
testImplementation("org.mockito:mockito-core:5.8.0")
```

**Purpose:** Mocking framework
**Why:** Create mock objects for testing
**Size:** N/A (test-only)
**Required:** No (but very useful)

**Features Used:**
- mock()
- verify()
- whenever()

---

### 22. Mockito Kotlin
```kotlin
testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
```

**Purpose:** Kotlin extensions for Mockito
**Why:** Better Mockito API for Kotlin
**Size:** N/A (test-only)
**Required:** No (when using Mockito)

**Features Used:**
- Kotlin-friendly DSL
- any() that works with Kotlin

---

## Android Testing Dependencies

### 23. AndroidX Test JUnit
```kotlin
androidTestImplementation("androidx.test.ext:junit:1.1.5")
```

**Purpose:** JUnit for Android instrumented tests
**Why:** Run tests on device/emulator
**Size:** N/A (test-only)
**Required:** Yes (for instrumented tests)

---

### 24. Espresso Core
```kotlin
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
```

**Purpose:** UI testing framework
**Why:** Test UI interactions
**Size:** N/A (test-only)
**Required:** No (for UI tests)

---

### 25. Compose UI Test JUnit4
```kotlin
androidTestImplementation("androidx.compose.ui:ui-test-junit4")
```

**Purpose:** Compose UI testing
**Why:** Test Compose UIs
**Size:** N/A (test-only)
**Required:** No (for Compose UI tests)
**Version:** Managed by BOM

---

## Debug Dependencies

### 26. Compose UI Tooling
```kotlin
debugImplementation("androidx.compose.ui:ui-tooling")
```

**Purpose:** Compose preview tooling
**Why:** Enable interactive preview
**Size:** ~1 MB (debug-only)
**Required:** No (dev convenience)
**Version:** Managed by BOM

---

### 27. Compose UI Test Manifest
```kotlin
debugImplementation("androidx.compose.ui:ui-test-manifest")
```

**Purpose:** Testing utilities manifest
**Why:** Support for UI tests
**Size:** ~50 KB (debug-only)
**Required:** No (for UI tests)
**Version:** Managed by BOM

---

## Build Dependencies

### Gradle Plugins

#### Android Gradle Plugin
```kotlin
id("com.android.application") version "8.2.0"
```

**Purpose:** Android build support
**Why:** Build Android applications
**Required:** Yes

---

#### Kotlin Android Plugin
```kotlin
id("org.jetbrains.kotlin.android") version "1.9.20"
```

**Purpose:** Kotlin support for Android
**Why:** Compile Kotlin code
**Required:** Yes

---

#### Kotlin Serialization Plugin
```kotlin
id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
```

**Purpose:** Kotlinx Serialization support
**Why:** Enable @Serializable annotation
**Required:** No (if not using kotlinx.serialization)

---

## Dependency Analysis

### Total APK Size Impact

**Estimated Breakdown:**
```
Core Android: ~2 MB
Jetpack Compose: ~10 MB
Navigation: ~1 MB
ViewModels: ~300 KB
Coroutines: ~1.5 MB
Gson: ~500 KB
Icons: ~5 MB
Other: ~1 MB
──────────────────
Total: ~21 MB (approximately)
```

**Note:** Actual size varies with ProGuard/R8 optimization

---

## Optimization Opportunities

### Potential Reductions

#### 1. Material Icons Extended
**Current:** 5 MB
**Alternative:** Use only core icons
**Savings:** ~4 MB

#### 2. Kotlinx DateTime
**Current:** 200 KB
**Alternative:** Remove (use java.time only)
**Savings:** 200 KB

#### 3. Accompanist System UI Controller
**Current:** 100 KB
**Alternative:** Direct API calls
**Savings:** 100 KB

**Total Potential Savings:** ~4.3 MB

---

## Dependency Updates

### Update Strategy
1. Review updates monthly
2. Test thoroughly before updating
3. Check breaking changes
4. Update documentation

### Critical Dependencies
Monitor closely:
- Compose BOM
- Kotlin version
- Android Gradle Plugin

### Update Commands
```bash
# Check for updates
./gradlew dependencyUpdates

# Update wrapper
./gradlew wrapper --gradle-version 8.2
```

---

## Security Considerations

### Dependency Security
- ✅ All from official repositories
- ✅ No deprecated libraries
- ✅ Regular updates
- ✅ No known vulnerabilities

### Repository Sources
```kotlin
repositories {
    google()        // Google's Maven repository
    mavenCentral()  // Maven Central
}
```

---

## License Compliance

### License Overview
- **Apache 2.0:** Most AndroidX, Kotlin
- **MIT:** Some testing libraries
- **BSD:** Mockito

**All compatible with commercial use**

---

## Future Dependencies

See [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) for planned additions:

### Planned
- Room Database
- Hilt (Dependency Injection)
- MPAndroidChart or Compose Charts
- DataStore (alternative to JSON)
- WorkManager (for notifications)

---

## Dependency Graph

```
app
├── Core Android
│   ├── core-ktx
│   ├── lifecycle-runtime-ktx
│   └── activity-compose
├── Jetpack Compose
│   ├── compose-bom
│   ├── ui
│   ├── material3
│   ├── icons-extended
│   └── tooling
├── Navigation
│   └── navigation-compose
├── ViewModel
│   ├── viewmodel-compose
│   └── runtime-compose
├── Coroutines
│   ├── coroutines-android
│   └── coroutines-core
├── Serialization
│   ├── gson
│   └── kotlinx-serialization-json
└── Testing
    ├── junit
    ├── mockito
    └── espresso
```

---

## Dependency Management Best Practices

### Version Catalogs (Future)
Consider migrating to Gradle version catalogs:
```kotlin
// libs.versions.toml
[versions]
compose = "1.6.0"
kotlin = "1.9.20"

[libraries]
compose-ui = { module = "androidx.compose.ui:ui", version.ref = "compose" }
```

---

## Related Documentation

- [SETUP.md](./SETUP.md) - Development setup
- [BUILD_DEPLOY.md](./BUILD_DEPLOY.md) - Build configuration
- [ARCHITECTURE.md](./ARCHITECTURE.md) - Architecture overview