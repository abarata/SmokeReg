# Development Setup Guide

## Overview

This guide will help you set up your development environment for working on SmokeReg.

---

## Prerequisites

### Required Software

#### 1. JDK (Java Development Kit)
- **Version:** JDK 8 or higher (JDK 11+ recommended)
- **Download:** [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)

**Verify Installation:**
```bash
java -version
javac -version
```

#### 2. Android Studio
- **Version:** Latest stable (Hedgehog 2023.1.1 or later recommended)
- **Download:** [Android Studio](https://developer.android.com/studio)

**Required Components:**
- Android SDK Platform 34
- Android SDK Build-Tools
- Android Emulator
- Android SDK Platform-Tools

#### 3. Android SDK
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Compile SDK:** 34

**Auto-installed with Android Studio**

#### 4. Git
- **Version:** Latest stable
- **Download:** [Git](https://git-scm.com/)

**Verify Installation:**
```bash
git --version
```

### Optional Software

#### Android Device
- Physical device for testing (recommended)
- USB cable
- USB debugging enabled

#### Command Line Tools
- Bash shell (Linux/Mac) or PowerShell (Windows)
- ADB (Android Debug Bridge)

---

## Project Setup

### 1. Clone/Open Project

#### If Starting from Scratch
Project is located at:
```bash
cd /home/abarata/projects/smokereg
```

#### Project Structure Verification
```bash
smokereg/
├── app/
│   ├── build.gradle.kts
│   └── src/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradlew.bat
```

### 2. Configure Android SDK Path

#### Option A: Android Studio (Automatic)
1. Open project in Android Studio
2. SDK path auto-configured

#### Option B: Manual (local.properties)
**File:** `local.properties`
```properties
sdk.dir=/path/to/Android/Sdk
```

**Common Paths:**
- **Linux:** `/home/username/Android/Sdk`
- **Mac:** `/Users/username/Library/Android/sdk`
- **Windows:** `C:\\Users\\username\\AppData\\Local\\Android\\Sdk`

**Current Configuration:**
```properties
sdk.dir=/home/abarata/Android/Sdk
```

### 3. Sync Gradle

#### In Android Studio
1. Open project
2. Click "Sync Project with Gradle Files" (🔄 icon)
3. Wait for sync to complete

#### Command Line
```bash
./gradlew --refresh-dependencies
```

---

## Building the Project

### Using Android Studio

#### Debug Build
1. Click "Build" → "Build Bundle(s) / APK(s)" → "Build APK(s)"
2. APK location: `app/build/outputs/apk/debug/app-debug.apk`

#### Release Build
1. Click "Build" → "Generate Signed Bundle / APK"
2. Follow signing wizard
3. APK location: `app/build/outputs/apk/release/`

### Using Command Line

#### Clean Build
```bash
./gradlew clean
```

#### Build Debug APK
```bash
./gradlew assembleDebug
```

**Output:** `app/build/outputs/apk/debug/app-debug.apk`

#### Build Release APK
```bash
./gradlew assembleRelease
```

**Output:** `app/build/outputs/apk/release/app-release.apk`

#### Install on Device
```bash
./gradlew installDebug
```

#### Run Tests
```bash
./gradlew test
```

### Using Build Script
```bash
chmod +x build_apk.sh
./build_apk.sh
```

**Output:** `SmokeReg-debug.apk` in project root

---

## Running the App

### In Android Studio

#### Using Emulator
1. Click "Device Manager"
2. Create/Select AVD (Android Virtual Device)
3. Click "Run" (▶ icon)

**Recommended AVD:**
- Device: Pixel 5
- System Image: Android 12 (API 31) or later
- RAM: 2048 MB

#### Using Physical Device
1. Enable Developer Options on device:
   - Settings → About Phone
   - Tap "Build Number" 7 times
2. Enable USB Debugging:
   - Settings → Developer Options → USB Debugging
3. Connect device via USB
4. Click "Run" (▶ icon)
5. Select device from list

### Using Command Line

#### Install and Run
```bash
# Install
adb install app/build/outputs/apk/debug/app-debug.apk

# Launch
adb shell am start -n com.smokereg/.MainActivity
```

#### View Logs
```bash
adb logcat | grep "SmokeReg"
```

---

## Development Tools

### Android Studio Configuration

#### Recommended Plugins
1. **Kotlin** (pre-installed)
2. **Android** (pre-installed)
3. **Jetpack Compose Preview** (pre-installed)
4. **Rainbow Brackets** - Better code readability
5. **Key Promoter X** - Learn shortcuts

#### Code Style
File → Settings → Editor → Code Style → Kotlin
- Use project's .editorconfig if available
- 4 spaces for indentation
- 120 character line limit

#### Compose Preview
- Enable "Interactive Mode" for preview
- Use Split view (Code + Preview)

### Gradle Configuration

#### Gradle Wrapper Version
```properties
# gradle/wrapper/gradle-wrapper.properties
distributionUrl=https://services.gradle.org/distributions/gradle-8.2-bin.zip
```

#### Gradle Properties
```properties
# gradle.properties
org.gradle.jvmargs=-Xmx2048m
org.gradle.parallel=true
android.useAndroidX=true
kotlin.code.style=official
```

---

## Troubleshooting Setup

### Issue: Gradle Sync Failed

#### Cause: SDK Not Found
**Solution:**
```bash
# Verify local.properties
cat local.properties

# If missing, create:
echo "sdk.dir=/path/to/Android/Sdk" > local.properties
```

#### Cause: Network Issues
**Solution:**
```bash
# Add to gradle.properties
systemProp.http.proxyHost=your.proxy.host
systemProp.http.proxyPort=8080
```

### Issue: Build Failed

#### Cause: Java Version Mismatch
**Solution:**
```bash
# Check Java version
java -version

# Should be 8 or higher
# If not, install correct version
```

#### Cause: Missing Dependencies
**Solution:**
```bash
# Force dependency refresh
./gradlew clean build --refresh-dependencies
```

### Issue: Cannot Install APK

#### Cause: Signature Conflict
**Solution:**
```bash
# Uninstall existing app
adb uninstall com.smokereg

# Reinstall
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### Cause: Device Not Authorized
**Solution:**
```bash
# Reset ADB
adb kill-server
adb start-server

# Reconnect device, accept authorization on device
```

---

## Environment Variables

### Optional Configuration

#### ANDROID_HOME
```bash
# Linux/Mac
export ANDROID_HOME=/path/to/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Windows (PowerShell)
$env:ANDROID_HOME = "C:\Users\username\AppData\Local\Android\Sdk"
$env:PATH += ";$env:ANDROID_HOME\platform-tools"
```

#### JAVA_HOME
```bash
# Linux/Mac
export JAVA_HOME=/path/to/jdk
export PATH=$PATH:$JAVA_HOME/bin

# Windows (PowerShell)
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"
$env:PATH += ";$env:JAVA_HOME\bin"
```

---

## IDE Configuration

### Recommended Settings

#### Memory Settings
File → Settings → Appearance & Behavior → System Settings
- Maximum heap size: 2048 MB
- Code cache size: 1024 MB

#### Build Settings
File → Settings → Build, Execution, Deployment → Compiler
- Build process heap size: 2048 MB
- ✓ Compile independent modules in parallel

#### Kotlin Compiler
File → Settings → Build, Execution, Deployment → Compiler → Kotlin Compiler
- ✓ Keep incremental compilation up to date
- ✓ Compile in parallel

---

## Testing Setup

### Unit Test Configuration

#### Run Single Test
```bash
./gradlew test --tests "com.smokereg.model.SmokeEntryTest"
```

#### Run All Tests
```bash
./gradlew test
```

#### Test Reports
Location: `app/build/reports/tests/testDebugUnitTest/index.html`

### Android Instrumented Tests

#### Prerequisites
- Emulator or device running
- USB debugging enabled

#### Run Tests
```bash
./gradlew connectedAndroidTest
```

#### Test Reports
Location: `app/build/reports/androidTests/connected/index.html`

---

## Version Control Setup

### Git Configuration

#### Initialize (if needed)
```bash
git init
git remote add origin <repository-url>
```

#### .gitignore
Verify `.gitignore` includes:
```
*.iml
.gradle
/local.properties
/.idea
.DS_Store
/build
/captures
*.apk
*.aab
```

#### First Commit
```bash
git add .
git commit -m "Initial commit"
git push -u origin main
```

---

## Team Setup

### Code Review Process
1. Create feature branch
2. Make changes
3. Run tests
4. Create pull request
5. Code review
6. Merge to main

### Branch Naming
- `feature/feature-name`
- `bugfix/bug-description`
- `hotfix/critical-fix`

---

## CI/CD Setup (Future)

See [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) for planned CI/CD integration.

---

## Verification Checklist

After setup, verify:
- [ ] Project opens in Android Studio without errors
- [ ] Gradle sync completes successfully
- [ ] Debug build compiles
- [ ] App runs on emulator
- [ ] App runs on physical device (if available)
- [ ] Unit tests pass
- [ ] All dependencies downloaded

---

## Quick Start Command Summary

```bash
# Navigate to project
cd /home/abarata/projects/smokereg

# Clean build
./gradlew clean

# Build debug APK
./gradlew assembleDebug

# Run tests
./gradlew test

# Install on device
./gradlew installDebug

# Or use build script
./build_apk.sh
```

---

## Getting Help

### Resources
- [Android Developer Documentation](https://developer.android.com/docs)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- Project documentation in `_documentation/` folder

### Common Issues
See [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) for detailed solutions.

---

## Related Documentation

- [BUILD_DEPLOY.md](./BUILD_DEPLOY.md) - Building and deployment
- [TESTING.md](./TESTING.md) - Testing guide
- [ARCHITECTURE.md](./ARCHITECTURE.md) - Project architecture
- [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) - Common problems