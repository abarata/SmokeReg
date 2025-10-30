# SmokeReg - Android Smoking Tracker App

## Overview
SmokeReg is a standalone Android application designed to help users track their smoking habits. The app operates completely offline, storing all data locally on the device in JSON format.

## Features

### Core Functionality
- **Quick Registration**: One-tap smoke registration with current timestamp
- **Avoidable Tracking**: Mark smokes as "avoidable" to identify unnecessary smoking
- **Custom Time Entry**: Adjust date/time for backdated entries
- **Date Navigation**: View and edit entries from any day (not just today)
- **Edit & Delete**: Modify or remove existing entries
- **Offline Storage**: All data stored locally in JSON format
- **Custom App Icon**: Distinctive lit cigar design

### Dashboard Statistics
Track your smoking patterns across different time periods:
- Today's count
- Yesterday's count
- This week's total
- This month's total
- This year's total

Each statistic shows both total count and avoidable smokes.
Features a refresh button to update statistics instantly.

## Technical Details

### Architecture
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material3
- **Architecture Pattern**: MVVM (Model-View-ViewModel)
- **Data Storage**: Local JSON file
- **Navigation**: Jetpack Navigation Compose
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 15)
- **Compile SDK**: 36

### Documentation
For comprehensive documentation, see the **`_documentation/`** folder:
- **[Documentation Index](_documentation/README.md)** - Complete overview
- **[Architecture](_documentation/ARCHITECTURE.md)** - MVVM architecture details
- **[UI Design](_documentation/UI.md)** - User interface and components
- **[Setup Guide](_documentation/SETUP.md)** - Development environment setup
- **[Build & Deploy](_documentation/BUILD_DEPLOY.md)** - Building and deployment
- **[Troubleshooting](_documentation/TROUBLESHOOTING.md)** - Common issues and solutions
- **[Git Workflow](_documentation/GIT_WORKFLOW.md)** - Git operations and rules
- And more...

### ⚠️ Version Control Rule

**CRITICAL:** All Git operations (commit, push, pull, merge) to this repository require **EXPLICIT user permission**. See [GIT_WORKFLOW.md](_documentation/GIT_WORKFLOW.md) for complete rules.

### Project Structure
```
app/
├── src/main/java/com/smokereg/
│   ├── MainActivity.kt              # Main activity
│   ├── SmokeRegApplication.kt       # Application class
│   ├── data/                        # Data layer
│   │   ├── JsonStorageManager.kt    # JSON file operations
│   │   └── SmokeRepository.kt       # Repository pattern
│   ├── model/                       # Data models
│   │   └── SmokeEntry.kt            # Smoke entry model
│   ├── navigation/                  # Navigation
│   │   ├── NavigationGraph.kt       # Navigation setup
│   │   └── Screen.kt                # Screen routes
│   ├── ui/                          # UI layer
│   │   ├── components/              # Reusable components
│   │   │   ├── BottomNavBar.kt
│   │   │   ├── SmokeEntryCard.kt
│   │   │   └── StatCard.kt
│   │   ├── screens/                 # App screens
│   │   │   ├── DashboardScreen.kt
│   │   │   ├── EditEntryDialog.kt
│   │   │   ├── MainScreen.kt
│   │   │   └── TimeAdjustDialog.kt
│   │   └── theme/                   # Material theme
│   │       ├── Theme.kt
│   │       └── Typography.kt
│   └── viewmodel/                   # ViewModels
│       ├── DashboardViewModel.kt
│       └── SmokeViewModel.kt
└── src/test/                        # Unit tests
```

## Building the App

### Prerequisites
- Android Studio (latest version recommended)
- Android SDK (API 34)
- JDK 8 or higher

### Build Instructions

#### Option 1: Using the Build Script
```bash
# Make the script executable (first time only)
chmod +x build_apk.sh

# Run the build script
./build_apk.sh
```

#### Option 2: Manual Build
```bash
# Clean previous builds
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# APK will be located at:
# app/build/outputs/apk/debug/app-debug.apk
```

#### Option 3: Android Studio
1. Open the project in Android Studio
2. Click "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
3. The APK will be generated in `app/build/outputs/apk/debug/`

## Installation

### On Android Device
1. Enable "Install from Unknown Sources" in device settings
2. Transfer the APK file to your device
3. Open the APK file to install

### Using ADB
```bash
adb install SmokeReg-debug.apk
```

## Usage

### Main Screen
1. **Navigate Dates**: Use the date selector to view entries from any day
   - ◀ Previous Day button
   - 📅 Calendar picker for any date
   - 🏠 Today button (when viewing other dates)
   - ▶ Next Day button (when not viewing future)
2. **Register a Smoke**: Tap the "Register Smoke" button to instantly log a smoke with the current timestamp
3. **Mark as Avoidable**: Check the "This was an avoidable smoke" checkbox before registering
4. **Custom Time**: Use "Adjust Time" to register a smoke with a different date/time
5. **View Entries**: All entries for the selected date are listed chronologically (newest first)
6. **Edit Entry**: Tap the edit icon on any entry to modify or delete it

### Dashboard
1. Navigate to Dashboard using the bottom navigation bar
2. View statistics for different time periods
3. Each card shows total count and avoidable smokes
4. Refresh statistics using the refresh button in the top bar

## Data Storage

### Location
Data is stored in the app's internal storage at:
```
/data/data/com.smokereg/files/smoke_entries.json
```

### Format
```json
[
  {
    "id": "unique-id",
    "dateTime": "2025-10-28T14:30:00",
    "isAvoidable": false
  }
]
```

## Testing

Run unit tests:
```bash
./gradlew test
```

Run instrumented tests:
```bash
./gradlew connectedAndroidTest
```

## Privacy

- All data is stored locally on your device
- No internet connection required
- No data is sent to external servers
- Data can be backed up through Android's backup system

## License

This project is provided as-is for personal use.

## Version History

### Version 1.1 (2025-10-30)
- ✅ Added date navigation feature - view entries from any day
- ✅ Fixed dashboard refresh infinite loading issue
- ✅ Custom app icon with lit cigar design on pastel red background
- ✅ Improved list ordering (most recent entries first)
- ✅ Updated to SDK 36 (Android 15)

### Version 1.0 (2025-10-28)
- Initial release with core functionality

## Support

For issues or questions, please refer to:
1. **[Troubleshooting Guide](_documentation/TROUBLESHOOTING.md)** - Common issues and solutions
2. **[Complete Documentation](_documentation/README.md)** - Full technical documentation
3. Rebuild the app from source if needed