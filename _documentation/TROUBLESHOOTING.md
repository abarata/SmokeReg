# Troubleshooting Guide

## Overview

Common issues and their solutions for SmokeReg development and deployment.

---

## Build Issues

### Issue: Gradle Sync Failed

#### Symptom
```
Could not resolve all files for configuration ':app:debugCompileClasspath'
```

#### Possible Causes & Solutions

**1. Network Issues**
```bash
# Solution: Clear Gradle cache
./gradlew clean --refresh-dependencies

# Or manually delete cache
rm -rf ~/.gradle/caches/
```

**2. SDK Path Not Set**
```bash
# Solution: Check local.properties
cat local.properties

# Should contain:
sdk.dir=/path/to/Android/Sdk

# If missing, add it:
echo "sdk.dir=/home/username/Android/Sdk" > local.properties
```

**3. Incompatible Gradle Version**
```bash
# Solution: Update Gradle wrapper
./gradlew wrapper --gradle-version 8.2
```

---

### Issue: Build Failed with Compilation Errors

#### Symptom
```
Compilation failed; see the compiler error output for details
```

#### Solutions

**1. Clean and Rebuild**
```bash
./gradlew clean build
```

**2. Invalidate Caches (Android Studio)**
```
File → Invalidate Caches / Restart → Invalidate and Restart
```

**3. Check Kotlin Version**
```kotlin
// In build.gradle.kts (root)
id("org.jetbrains.kotlin.android") version "1.9.20"
```

**4. Verify Java Version**
```bash
java -version
# Should be 8 or higher
```

---

### Issue: "Unresolved reference: Composable"

#### Symptom
```
Unresolved reference: Composable
Unresolved reference: viewModel
```

#### Solutions

**1. Ensure Compose is Enabled**
```kotlin
// In app/build.gradle.kts
buildFeatures {
    compose = true
}

composeOptions {
    kotlinCompilerExtensionVersion = "1.5.4"
}
```

**2. Check Dependencies**
```kotlin
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
```

**3. Sync Project with Gradle Files**
```
Tools → Gradle → Sync Now
```

---

## Runtime Issues

### Issue: App Crashes on Launch

#### Symptom
```
java.lang.RuntimeException: Unable to start activity
```

#### Solutions

**1. Check AndroidManifest**
```xml
<!-- Verify MainActivity is declared -->
<activity
    android:name=".MainActivity"
    android:exported="true">
</activity>

<!-- Verify Application class -->
<application
    android:name=".SmokeRegApplication"
```

**2. Check Logs**
```bash
adb logcat | grep "SmokeReg"
```

**3. Verify Permissions**
- App doesn't require special permissions
- Internal storage access is automatic

---

### Issue: "Application not initialized"

#### Symptom
```
java.lang.IllegalStateException: Application not initialized
```

#### Solutions

**1. Verify AndroidManifest**
```xml
<application
    android:name=".SmokeRegApplication"  <!-- Must match class name -->
```

**2. Check Application Class**
```kotlin
class SmokeRegApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this  // Ensure instance is set
    }
}
```

---

### Issue: Data Not Persisting

#### Symptom
- Entries disappear after app restart
- Data not saving

#### Solutions

**1. Check File Write Success**
```kotlin
val success = repository.addEntry(isAvoidable)
if (!success) {
    // Handle error
}
```

**2. Verify Storage Path**
```kotlin
// In JsonStorageManager
private val file: File
    get() = File(context.filesDir, "smoke_entries.json")

// Test file exists
Log.d("SmokeReg", "File path: ${file.absolutePath}")
Log.d("SmokeReg", "File exists: ${file.exists()}")
```

**3. Check for Exceptions**
```kotlin
try {
    storageManager.writeEntries(entries)
} catch (e: Exception) {
    Log.e("SmokeReg", "Write failed", e)
}
```

**4. Verify JSON Format**
```bash
# Pull file from device
adb shell "run-as com.smokereg cat /data/data/com.smokereg/files/smoke_entries.json"

# Should be valid JSON array
```

---

### Issue: UI Not Updating

#### Symptom
- New entries don't appear in list
- Statistics don't update

#### Solutions

**1. Verify StateFlow Collection**
```kotlin
val entries by viewModel.todaysEntries.collectAsState()
// Not: val entries = viewModel.todaysEntries.value
```

**2. Check Repository Reload**
```kotlin
suspend fun addEntry(entry: SmokeEntry): Boolean {
    val success = storageManager.addEntry(entry)
    if (success) {
        loadEntries()  // Must reload to update flow
    }
    return success
}
```

**3. Verify ViewModel Scope**
```kotlin
viewModelScope.launch {  // Must use viewModelScope
    repository.addEntry(entry)
}
```

---

### Issue: Dashboard Refresh Loading Infinitely

#### Symptom
- Click dashboard refresh button
- Loading spinner appears
- Loading never stops, UI frozen

#### Root Cause
StateFlow only emits when the value actually changes. If the data hasn't changed between refreshes, the flow doesn't emit a new value, and any collector waiting for an emission will never receive it. This causes the loading state to never be reset.

#### Solution
Force immediate recalculation after loading data using `.first()` to get the current value without waiting for an emission:

```kotlin
// In DashboardViewModel.kt
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

**Key Points:**
- Don't rely on collector to reset loading state
- Use `.first()` to get current flow value immediately
- Ensures loading state is always reset, even if data unchanged
- File location: `app/src/main/java/com/smokereg/viewmodel/DashboardViewModel.kt:79-92`

---

## Installation Issues

### Issue: Cannot Install APK

#### Symptom
```
INSTALL_FAILED_UPDATE_INCOMPATIBLE
```

#### Solutions

**1. Uninstall Existing App**
```bash
adb uninstall com.smokereg
```

**2. Install Fresh**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

### Issue: "App not installed" on Device

#### Symptom
- Installation fails silently
- "App not installed" error

#### Solutions

**1. Enable Unknown Sources**
```
Settings → Security → Install Unknown Apps → Enable for your file manager
```

**2. Check Available Storage**
```
Settings → Storage → Ensure sufficient space
```

**3. Verify APK Integrity**
```bash
# Check APK is not corrupted
unzip -t app-debug.apk
```

---

### Issue: USB Debugging Not Working

#### Symptom
- Device not recognized by ADB
- "No permissions" error

#### Solutions

**1. Enable Developer Options**
```
Settings → About Phone → Tap "Build Number" 7 times
```

**2. Enable USB Debugging**
```
Settings → Developer Options → USB Debugging → Enable
```

**3. Authorize Computer**
```bash
adb devices
# Shows "unauthorized" → Accept prompt on device
```

**4. Reset ADB**
```bash
adb kill-server
adb start-server
```

**5. Check USB Cable**
- Use data cable (not charge-only)
- Try different USB port
- Try different cable

---

## Development Issues

### Issue: Compose Preview Not Working

#### Symptom
- Preview shows "No preview found"
- Preview not updating

#### Solutions

**1. Rebuild Project**
```bash
./gradlew clean build
```

**2. Verify Preview Annotations**
```kotlin
@Preview(showBackground = true)
@Composable
fun MyPreview() {
    SmokeRegTheme {
        // Content
    }
}
```

**3. Check Dependencies**
```kotlin
debugImplementation("androidx.compose.ui:ui-tooling")
implementation("androidx.compose.ui:ui-tooling-preview")
```

**4. Invalidate Caches**
```
File → Invalidate Caches / Restart
```

---

### Issue: Slow Build Times

#### Symptom
- Gradle build takes too long
- Sync is very slow

#### Solutions

**1. Enable Parallel Builds**
```properties
# In gradle.properties
org.gradle.parallel=true
org.gradle.caching=true
```

**2. Increase Heap Size**
```properties
# In gradle.properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
```

**3. Enable Configuration Cache**
```properties
org.gradle.configuration-cache=true
```

**4. Clean Old Builds**
```bash
./gradlew clean
rm -rf build/
rm -rf app/build/
```

---

## Testing Issues

### Issue: Tests Not Running

#### Symptom
- "No tests found" error
- Tests fail to execute

#### Solutions

**1. Verify Test Location**
```
app/src/test/java/com/smokereg/  ← Correct
app/src/main/java/com/smokereg/  ← Wrong
```

**2. Check Test Annotations**
```kotlin
@Test  // Must have this annotation
fun testMethod() {
    // test code
}
```

**3. Verify Dependencies**
```kotlin
testImplementation("junit:junit:4.13.2")
```

**4. Run Specific Test**
```bash
./gradlew test --tests "com.smokereg.model.SmokeEntryTest"
```

---

### Issue: Coroutine Tests Failing

#### Symptom
```
IllegalStateException: Module with the Main dispatcher is missing
```

#### Solutions

**1. Set Up Test Dispatcher**
```kotlin
@Before
fun setup() {
    Dispatchers.setMain(UnconfinedTestDispatcher())
}

@After
fun tearDown() {
    Dispatchers.resetMain()
}
```

**2. Use runTest**
```kotlin
@Test
fun myTest() = runTest {
    // Test code with coroutines
}
```

---

## Performance Issues

### Issue: App Feels Sluggish

#### Solutions

**1. Check Main Thread**
```kotlin
// Don't do this on main thread:
val entries = storageManager.readEntries()  // ❌ Blocks UI

// Do this instead:
viewModelScope.launch {
    val entries = storageManager.readEntries()  // ✅ On IO thread
}
```

**2. Optimize Recomposition**
```kotlin
// Use remember for expensive computations
val stats = remember(entries) {
    calculateStats(entries)
}
```

**3. Profile App**
```bash
# Use Android Studio Profiler
View → Tool Windows → Profiler
```

---

### Issue: High Memory Usage

#### Solutions

**1. Check for Leaks**
- Use LeakCanary (optional dependency)
- Profile with Android Studio

**2. Limit List Size**
```kotlin
// If list is very long, use paging
LazyColumn {
    items(entries.take(100)) { entry ->
        // Show limited items initially
    }
}
```

---

## Common Error Messages

### "AAPT: error: resource ... not found"

**Cause:** Missing resource
**Solution:**
```bash
# Clean and rebuild
./gradlew clean assembleDebug
```

### "Manifest merger failed"

**Cause:** Conflicting manifest entries
**Solution:**
```xml
<!-- Add to AndroidManifest.xml -->
<uses-sdk tools:overrideLibrary="conflicting.library"/>
```

### "Out of memory error"

**Cause:** Insufficient heap size
**Solution:**
```properties
# Increase in gradle.properties
org.gradle.jvmargs=-Xmx4096m
```

---

## Device-Specific Issues

### Samsung Devices

**Issue:** App crashes on Samsung
**Solution:** Test battery optimization settings
```
Settings → Apps → SmokeReg → Battery → Unrestricted
```

### Xiaomi/MIUI Devices

**Issue:** App killed in background
**Solution:** Disable battery optimization
```
Settings → Battery & Performance → App Battery Saver → SmokeReg → No restrictions
```

### Huawei Devices

**Issue:** Notifications not working (future feature)
**Solution:** Enable auto-start
```
Settings → Battery → App Launch → SmokeReg → Manual → Allow all
```

---

## Data Corruption Issues

### Issue: "Parse error" when reading JSON

#### Solutions

**1. Backup Data**
```bash
adb backup -f backup.ab -noapk com.smokereg
```

**2. Inspect JSON File**
```bash
adb shell "run-as com.smokereg cat /data/data/com.smokereg/files/smoke_entries.json"
```

**3. Validate JSON**
- Copy content to https://jsonlint.com/
- Fix syntax errors

**4. Restore or Clear**
```bash
# Clear data (WARNING: Deletes all entries)
adb shell pm clear com.smokereg

# Or restore from backup
adb restore backup.ab
```

---

## Debug Mode

### Enable Verbose Logging

```kotlin
// Add to code temporarily
private val DEBUG = true

if (DEBUG) {
    Log.d("SmokeReg", "Entries loaded: ${entries.size}")
    Log.d("SmokeReg", "File path: ${file.absolutePath}")
}
```

### View All Logs
```bash
adb logcat -c  # Clear logs
adb logcat | grep -E "(SmokeReg|AndroidRuntime)"
```

---

## Getting Help

### Information to Provide

When reporting issues, include:
1. Android version
2. Device model
3. Error message / stack trace
4. Steps to reproduce
5. Expected vs actual behavior

### Where to Get Help

1. Check this troubleshooting guide
2. Review related documentation
3. Search error message online
4. Check Android Studio logs
5. Create GitHub issue (if project is on GitHub)

---

## Prevention Best Practices

### Before Building
- ✅ Sync Gradle
- ✅ Check for errors in IDE
- ✅ Run tests
- ✅ Clean build occasionally

### Before Deploying
- ✅ Test on multiple devices
- ✅ Test with different Android versions
- ✅ Verify data persistence
- ✅ Check for memory leaks

### During Development
- ✅ Commit frequently
- ✅ Write tests
- ✅ Use version control
- ✅ Document changes

---

## Related Documentation

- [SETUP.md](./SETUP.md) - Initial setup
- [BUILD_DEPLOY.md](./BUILD_DEPLOY.md) - Build process
- [TESTING.md](./TESTING.md) - Testing guide
- [ARCHITECTURE.md](./ARCHITECTURE.md) - App architecture