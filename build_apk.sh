#!/bin/bash

echo "Building SmokeReg APK..."
echo "========================="

# Clean previous builds
echo "Cleaning previous builds..."
./gradlew clean

# Build debug APK
echo "Building debug APK..."
./gradlew assembleDebug

# Check if build was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "Build successful!"
    echo "=================="

    # Find and copy APK to root directory
    APK_PATH=$(find app/build/outputs/apk/debug -name "*.apk" | head -1)

    if [ -n "$APK_PATH" ]; then
        cp "$APK_PATH" ./SmokeReg-debug.apk
        echo "APK copied to: ./SmokeReg-debug.apk"
        echo ""
        echo "APK Details:"
        echo "============"
        ls -lh SmokeReg-debug.apk
        echo ""
        echo "Installation Instructions:"
        echo "=========================="
        echo "1. Enable 'Install from Unknown Sources' on your Android device"
        echo "2. Transfer SmokeReg-debug.apk to your device"
        echo "3. Open the file on your device to install"
        echo ""
        echo "Or install via ADB:"
        echo "adb install SmokeReg-debug.apk"
    else
        echo "Error: Could not find APK file"
        exit 1
    fi
else
    echo ""
    echo "Build failed! Please check the error messages above."
    exit 1
fi