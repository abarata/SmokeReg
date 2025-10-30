# Build & Deployment Documentation

## Overview

Complete guide for building, testing, and deploying the SmokeReg Android application.

---

## Build Variants

### Debug Build
- **Purpose:** Development and testing
- **Signing:** Debug keystore (auto-generated)
- **Minification:** Disabled
- **Debuggable:** Yes
- **ProGuard:** Disabled

### Release Build
- **Purpose:** Production distribution
- **Signing:** Release keystore (manual)
- **Minification:** Disabled (can be enabled)
- **Debuggable:** No
- **ProGuard:** Optional

---

## Building Debug APK

### Method 1: Gradle Command Line

#### Basic Build
```bash
cd /home/abarata/projects/smokereg
./gradlew assembleDebug
```

**Output Location:**
```
app/build/outputs/apk/debug/app-debug.apk
```

#### Clean Build
```bash
./gradlew clean assembleDebug
```

#### Verbose Build
```bash
./gradlew assembleDebug --info
```

---

### Method 2: Android Studio

1. **Open Project**
   - File → Open → Select project directory

2. **Build APK**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Wait for build to complete
   - Click "locate" in notification

3. **Output**
   - APK located at `app/build/outputs/apk/debug/`

---

### Method 3: Build Script

**Use Provided Script:**
```bash
chmod +x build_apk.sh
./build_apk.sh
```

**Script Actions:**
1. Cleans previous builds
2. Builds debug APK
3. Copies to project root as `SmokeReg-debug.apk`
4. Shows file info and installation instructions

**Output:**
```
./SmokeReg-debug.apk
```

---

## Building Release APK

### Prerequisites

#### 1. Create Release Keystore

```bash
keytool -genkey -v \
  -keystore smokereg-release.keystore \
  -alias smokereg \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

**Save Keystore Info:**
- Keystore password
- Key alias: smokereg
- Key password
- Store file securely (NOT in git)

---

#### 2. Configure Signing

**Create:** `keystore.properties` (in project root)
```properties
storePassword=your_store_password
keyPassword=your_key_password
keyAlias=smokereg
storeFile=/path/to/smokereg-release.keystore
```

**Add to `.gitignore`:**
```
keystore.properties
*.keystore
```

---

#### 3. Update Build Configuration

**Edit:** `app/build.gradle.kts`

```kotlin
// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    ...

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false  // Can enable for smaller APK
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

---

### Build Release APK

```bash
./gradlew assembleRelease
```

**Output Location:**
```
app/build/outputs/apk/release/app-release.apk
```

---

## Installing APK

### Method 1: Direct Device Installation

#### Prerequisites
- APK file
- Android device
- USB cable (or file transfer method)

#### Steps
1. **Transfer APK to Device**
   - Via USB
   - Via cloud storage
   - Via email

2. **Enable Unknown Sources**
   ```
   Settings → Security → Install Unknown Apps
   → [Your File Manager] → Enable
   ```

3. **Install APK**
   - Open APK file in file manager
   - Tap "Install"
   - Wait for installation
   - Tap "Open"

---

### Method 2: ADB Installation

#### Prerequisites
- ADB installed
- Device connected via USB
- USB debugging enabled

#### Install Debug APK
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### Install Release APK
```bash
adb install app/build/outputs/apk/release/app-release.apk
```

#### Replace Existing Installation
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

#### Install on Specific Device
```bash
# List devices
adb devices

# Install on specific device
adb -s DEVICE_SERIAL install app-debug.apk
```

---

### Method 3: Android Studio

1. **Connect Device** or **Start Emulator**

2. **Run App**
   - Click "Run" button (▶)
   - Or: Run → Run 'app'
   - Or: Shift+F10

3. **Select Device**
   - Choose from connected devices/emulators
   - App builds and installs automatically

---

## Verifying Installation

### Check Installed Version
```bash
adb shell pm list packages | grep smokereg
# Output: package:com.smokereg

adb shell dumpsys package com.smokereg | grep versionName
# Output: versionName=1.0
```

### Launch App
```bash
adb shell am start -n com.smokereg/.MainActivity
```

### View App Info
```bash
adb shell dumpsys package com.smokereg
```

---

## Running Tests

### Unit Tests

#### Run All Unit Tests
```bash
./gradlew test
```

#### Run Specific Test Class
```bash
./gradlew test --tests "com.smokereg.model.SmokeEntryTest"
```

#### Run Specific Test Method
```bash
./gradlew test --tests "com.smokereg.model.SmokeEntryTest.getTimeDisplay*"
```

#### Test Report
Location: `app/build/reports/tests/testDebugUnitTest/index.html`

```bash
# Open in browser
open app/build/reports/tests/testDebugUnitTest/index.html
# Or: xdg-open (Linux), start (Windows)
```

---

### Instrumented Tests

#### Prerequisites
- Device connected or emulator running

#### Run All Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

#### Test Report
Location: `app/build/reports/androidTests/connected/index.html`

---

### In Android Studio

1. **Right-click Test File**
2. **Run 'TestClassName'**
3. **View Results** in Run window

---

## Code Quality Checks

### Lint

#### Run Lint
```bash
./gradlew lint
```

#### Lint Report
Location: `app/build/reports/lint-results.html`

#### Fix Issues
```bash
./gradlew lintFix  # Auto-fix some issues
```

---

### ProGuard/R8

#### Enable Minification
```kotlin
// In app/build.gradle.kts
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
    }
}
```

#### Custom ProGuard Rules
File: `app/proguard-rules.pro`

```proguard
# Keep data classes
-keep class com.smokereg.model.** { *; }

# Keep Gson classes
-keep class com.google.gson.** { *; }
```

---

## APK Analysis

### APK Size
```bash
ls -lh app/build/outputs/apk/debug/app-debug.apk
```

### APK Analyzer (Android Studio)
```
Build → Analyze APK → Select APK file
```

**Shows:**
- APK size
- Download size (estimated)
- File breakdown
- Method count
- Resource sizes

---

## Build Optimization

### Reduce APK Size

#### 1. Enable R8
```kotlin
buildTypes {
    release {
        isMinifyEnabled = true
        isShrinkResources = true
    }
}
```

**Potential Savings:** 30-40% size reduction

---

#### 2. Use App Bundle
```bash
./gradlew bundleRelease
```

**Output:** `app/build/outputs/bundle/release/app-release.aab`

**Benefits:**
- Smaller download size
- Automatic APK splitting
- Required for Google Play

---

#### 3. Remove Unused Resources
```kotlin
android {
    buildTypes {
        release {
            isShrinkResources = true
        }
    }
}
```

---

#### 4. Use WebP Images
- Convert PNG/JPG to WebP
- Smaller file size
- Same quality

---

#### 5. Reduce Icon Set
Currently using Material Icons Extended (~5 MB)

**Option 1:** Use only needed icons
```kotlin
// Remove extended icons
// implementation("androidx.compose.material:material-icons-extended")

// Use vector assets for specific icons
```

**Savings:** ~4 MB

---

### Speed Up Builds

#### 1. Gradle Daemon
```properties
# gradle.properties
org.gradle.daemon=true
```

#### 2. Parallel Builds
```properties
org.gradle.parallel=true
```

#### 3. Configuration Cache
```properties
org.gradle.configuration-cache=true
```

#### 4. Increase Heap Size
```properties
org.gradle.jvmargs=-Xmx4096m
```

#### 5. Use Build Cache
```properties
org.gradle.caching=true
```

---

## CI/CD Integration (Future)

### GitHub Actions Example
```yaml
name: Android CI

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 11
        uses: actions/setup-java@v2
        with:
          java-version: '11'
      - name: Build with Gradle
        run: ./gradlew assembleDebug
      - name: Run tests
        run: ./gradlew test
      - name: Upload APK
        uses: actions/upload-artifact@v2
        with:
          name: app-debug
          path: app/build/outputs/apk/debug/app-debug.apk
```

---

## Distribution

### Internal Testing

#### 1. Direct APK Distribution
- Email APK to testers
- Upload to cloud storage
- Share via messaging apps

#### 2. Firebase App Distribution
```bash
# Install Firebase CLI
npm install -g firebase-tools

# Login
firebase login

# Upload APK
firebase appdistribution:distribute \
  app/build/outputs/apk/release/app-release.apk \
  --app YOUR_APP_ID \
  --groups testers
```

---

### Google Play Store (Future)

#### Requirements
1. Google Play Developer Account ($25 one-time fee)
2. Signed app bundle (.aab)
3. App listing details
4. Privacy policy

#### Steps
1. **Create App**
   - Google Play Console → Create app

2. **Upload Bundle**
   ```bash
   ./gradlew bundleRelease
   ```
   - Upload `app-release.aab`

3. **Complete Listing**
   - Screenshots
   - Description
   - Icon
   - Feature graphic

4. **Submit for Review**

---

## Versioning

### Version Code & Name

**Location:** `app/build.gradle.kts`

```kotlin
defaultConfig {
    versionCode = 1        // Increment for each release
    versionName = "1.0"    // Visible to users
}
```

### Version Strategy
- **Major:** Breaking changes (1.0 → 2.0)
- **Minor:** New features (1.0 → 1.1)
- **Patch:** Bug fixes (1.0.0 → 1.0.1)

---

## Release Checklist

### Before Release
- [ ] All tests pass
- [ ] Lint checks pass
- [ ] Version updated
- [ ] Changelog updated
- [ ] ProGuard rules tested
- [ ] Tested on multiple devices
- [ ] Tested on different Android versions
- [ ] All features working
- [ ] No debug code left
- [ ] Performance tested

### Build
- [ ] Clean build successful
- [ ] Release APK signed
- [ ] APK analyzed for size
- [ ] APK tested on device

### Deployment
- [ ] APK distributed to testers
- [ ] Feedback collected
- [ ] Issues fixed
- [ ] Final release built

---

## Rollback Strategy

### If Issues Found

1. **Stop Distribution**
   - Remove download links
   - Notify users

2. **Fix Issues**
   - Identify bug
   - Write fix
   - Test thoroughly

3. **Release Patch**
   - Increment version
   - Build new APK
   - Distribute update

4. **Communicate**
   - Notify users of update
   - Explain fix

---

## Build Artifacts

### Keeping Builds

#### Structure
```
builds/
├── v1.0/
│   ├── app-debug.apk
│   ├── app-release.apk
│   ├── mapping.txt         # ProGuard mappings
│   └── release-notes.txt
└── v1.1/
    └── ...
```

#### ProGuard Mappings
**Important:** Save `mapping.txt` for each release
- Needed to deobfuscate crash reports
- Cannot be regenerated

**Location:** `app/build/outputs/mapping/release/mapping.txt`

---

## Environment-Specific Builds

### Build Flavors (Future)
```kotlin
android {
    flavorDimensions += "version"
    productFlavors {
        create("free") {
            dimension = "version"
            applicationIdSuffix = ".free"
        }
        create("pro") {
            dimension = "version"
            applicationIdSuffix = ".pro"
        }
    }
}
```

---

## Related Documentation

- [SETUP.md](./SETUP.md) - Development setup
- [TESTING.md](./TESTING.md) - Testing guide
- [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) - Common issues
- [DEPENDENCIES.md](./DEPENDENCIES.md) - Dependencies list