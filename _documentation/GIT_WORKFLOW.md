# Git Workflow & GitHub Integration

**Repository URL:** https://github.com/abarata/SmokeReg.git
**Last Updated:** 2025-10-30

---

## 📋 Table of Contents

1. [Initial Setup](#initial-setup)
2. [Daily Workflow](#daily-workflow)
3. [Branching Strategy](#branching-strategy)
4. [Common Operations](#common-operations)
5. [Troubleshooting](#troubleshooting)
6. [Best Practices](#best-practices)

---

## Initial Setup

### First Time Configuration

```bash
# Set your Git identity (if not already set globally)
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Verify configuration
git config --global --list
```

### Clone Repository (If Starting Fresh)

```bash
# Clone the repository
git clone https://github.com/abarata/SmokeReg.git

# Navigate to project
cd SmokeReg
```

### Initialize Existing Project (Already Done)

```bash
# Initialize Git repository
git init

# Add remote repository
git remote add origin https://github.com/abarata/SmokeReg.git

# Verify remote
git remote -v

# Output should show:
# origin  https://github.com/abarata/SmokeReg.git (fetch)
# origin  https://github.com/abarata/SmokeReg.git (push)
```

---

## Daily Workflow

### 1. Check Status

```bash
# See what files have changed
git status

# See detailed changes
git diff
```

### 2. Stage Changes

```bash
# Stage specific files
git add path/to/file.kt

# Stage all changes
git add .

# Stage all files in a directory
git add app/src/main/java/com/smokereg/

# Stage files by pattern
git add *.kt
```

### 3. Commit Changes

```bash
# Commit with inline message
git commit -m "Add date navigation feature"

# Commit with detailed message
git commit -m "Fix dashboard refresh loading issue

- Use .first() to get current flow value
- Ensures loading state always resets
- Fixes infinite loading spinner
"

# Amend last commit (if you forgot something)
git commit --amend
```

### 4. Push to GitHub

```bash
# Push to main branch
git push origin main

# Push and set upstream (first time)
git push -u origin main

# Force push (use with caution!)
git push --force origin main
```

### 5. Pull Changes

```bash
# Pull latest changes
git pull origin main

# Pull with rebase (cleaner history)
git pull --rebase origin main
```

---

## Branching Strategy

### Main Branch

**Branch:** `main`
- Production-ready code
- All features tested and working
- Direct commits allowed for small fixes
- All major features should come from feature branches

### Feature Branches

```bash
# Create and switch to new branch
git checkout -b feature/date-navigation

# Or create branch without switching
git branch feature/date-navigation

# Switch to existing branch
git checkout feature/date-navigation

# Push feature branch to GitHub
git push -u origin feature/date-navigation

# List all branches
git branch -a

# Delete local branch (after merging)
git branch -d feature/date-navigation

# Delete remote branch
git push origin --delete feature/date-navigation
```

### Branch Naming Convention

```
feature/feature-name       - New features
bugfix/issue-description   - Bug fixes
hotfix/critical-fix        - Critical production fixes
docs/documentation-update  - Documentation changes
refactor/code-improvement  - Code refactoring
```

**Examples:**
- `feature/date-navigation`
- `bugfix/dashboard-refresh`
- `docs/update-readme`
- `hotfix/crash-on-startup`

---

## Common Operations

### Viewing History

```bash
# View commit history
git log

# View compact history
git log --oneline

# View last 5 commits
git log -5

# View graphical history
git log --graph --oneline --all

# View changes in specific commit
git show <commit-hash>

# View file history
git log -- path/to/file.kt
```

### Undoing Changes

```bash
# Discard changes in working directory
git checkout -- path/to/file.kt

# Unstage file (keep changes)
git reset HEAD path/to/file.kt

# Undo last commit (keep changes)
git reset --soft HEAD~1

# Undo last commit (discard changes) - DANGEROUS!
git reset --hard HEAD~1

# Revert commit (creates new commit)
git revert <commit-hash>
```

### Stashing Changes

```bash
# Save current changes temporarily
git stash

# Save with message
git stash save "Work in progress on date picker"

# List stashes
git stash list

# Apply last stash (keep in stash)
git stash apply

# Apply and remove from stash
git stash pop

# Apply specific stash
git stash apply stash@{1}

# Delete stash
git stash drop stash@{0}

# Clear all stashes
git stash clear
```

### Merging

```bash
# Merge feature branch into current branch
git merge feature/date-navigation

# Merge with commit message
git merge feature/date-navigation -m "Merge date navigation feature"

# Abort merge if conflicts
git merge --abort

# Continue merge after resolving conflicts
git merge --continue
```

### Resolving Conflicts

```bash
# When merge conflict occurs:

# 1. View conflicted files
git status

# 2. Open files and resolve conflicts
#    Look for markers: <<<<<<<, =======, >>>>>>>
#    Keep desired changes, remove markers

# 3. Stage resolved files
git add path/to/resolved-file.kt

# 4. Complete merge
git commit -m "Resolve merge conflicts"
```

### Tagging Releases

```bash
# Create annotated tag
git tag -a v1.1 -m "Version 1.1 - Date navigation and fixes"

# Create lightweight tag
git tag v1.1

# Push tag to GitHub
git push origin v1.1

# Push all tags
git push origin --tags

# List tags
git tag

# Delete local tag
git tag -d v1.1

# Delete remote tag
git push origin --delete v1.1
```

---

## GitHub-Specific Operations

### Syncing Fork

```bash
# Add upstream remote (if forked)
git remote add upstream https://github.com/original/repo.git

# Fetch upstream changes
git fetch upstream

# Merge upstream into your branch
git merge upstream/main
```

### Creating Pull Request

```bash
# 1. Create and push feature branch
git checkout -b feature/new-feature
# ... make changes ...
git add .
git commit -m "Add new feature"
git push -u origin feature/new-feature

# 2. Go to GitHub and create Pull Request
# 3. After approval and merge, update local main
git checkout main
git pull origin main

# 4. Delete feature branch
git branch -d feature/new-feature
git push origin --delete feature/new-feature
```

### Managing Releases

```bash
# Tag release
git tag -a v1.1 -m "Version 1.1"
git push origin v1.1

# GitHub will create release from tag
# Add release notes on GitHub interface
# Attach APK file to release
```

---

## Project-Specific Files

### .gitignore

Already configured to ignore:

```
# Build outputs
*.iml
.gradle
/local.properties
/.idea/
.DS_Store
/build
/captures
*.apk (except releases)

# Generated files
*.ap_
*.dex
*.class

# Gradle files
.gradle/
build/

# Local configuration
local.properties

# Android Studio
.idea/
*.iml
.externalNativeBuild
.cxx

# Keystore files
*.jks
*.keystore
```

### Files to Always Commit

```
✅ Source code (.kt files)
✅ Resources (res/)
✅ Gradle wrapper (gradlew, gradle-wrapper.jar)
✅ Build scripts (build.gradle.kts, settings.gradle.kts)
✅ Manifest (AndroidManifest.xml)
✅ Documentation (_documentation/)
✅ README.md
✅ .gitignore

❌ Don't commit:
- Build outputs (build/, *.apk)
- local.properties
- .idea/ (Android Studio settings)
- .gradle/ (Gradle cache)
```

---

## Troubleshooting

### Issue: Push Rejected

```bash
# Error: Updates were rejected because remote contains work that you do not have

# Solution 1: Pull and merge
git pull origin main
# Resolve conflicts if any
git push origin main

# Solution 2: Pull with rebase
git pull --rebase origin main
git push origin main

# Solution 3: Force push (DANGEROUS - only if you're sure!)
git push --force origin main
```

### Issue: Wrong Commit Message

```bash
# If commit not yet pushed
git commit --amend -m "Correct message"

# If already pushed (not recommended)
git commit --amend -m "Correct message"
git push --force origin main
```

### Issue: Committed Wrong Files

```bash
# Before pushing
git reset --soft HEAD~1
# Fix files
git add correct-files
git commit -m "Correct commit"

# After pushing
git revert <commit-hash>
git push origin main
```

### Issue: Need to Undo Push

```bash
# Create revert commit
git revert <commit-hash>
git push origin main

# Or reset (DANGEROUS - rewrites history)
git reset --hard <previous-commit-hash>
git push --force origin main
```

### Issue: Large File Rejected

```bash
# Error: File size exceeds GitHub's limit (100MB)

# Solution: Remove from history
git filter-branch --tree-filter 'rm -f path/to/large-file' HEAD
git push --force origin main

# Or use BFG Repo-Cleaner
```

### Issue: Merge Conflicts

```bash
# 1. See conflicted files
git status

# 2. Open files and look for:
<<<<<<< HEAD
your changes
=======
their changes
>>>>>>> branch-name

# 3. Keep desired changes, remove markers

# 4. Stage and commit
git add resolved-file.kt
git commit -m "Resolve merge conflicts"
```

---

## Best Practices

### Commit Messages

**Good commit messages:**
```
✅ Add date navigation feature to main screen
✅ Fix dashboard refresh infinite loading issue
✅ Update UI documentation with new features
✅ Refactor JsonStorageManager for better performance
✅ Remove unused imports from SmokeViewModel
```

**Bad commit messages:**
```
❌ Fixed stuff
❌ Update
❌ Changes
❌ WIP
❌ asdfasdf
```

**Convention:**
```
<type>: <subject>

<body>

<footer>
```

**Types:**
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation
- `refactor:` Code refactoring
- `test:` Adding tests
- `chore:` Maintenance tasks

**Example:**
```
feat: Add date navigation to main screen

- Add date selector with previous/next buttons
- Integrate calendar picker
- Update ViewModel with date selection logic
- Filter entries by selected date

Closes #123
```

### Commit Frequency

```bash
# Commit often, push strategically

✅ Good practice:
git add .
git commit -m "Add date selector UI"
# ... continue working ...
git commit -m "Add date navigation logic"
# ... continue working ...
git commit -m "Update tests for date navigation"
git push origin main

❌ Bad practice:
# Work for days without committing
git add .
git commit -m "All changes"
git push origin main
```

### Branch Management

```bash
# Keep branches focused
✅ feature/date-navigation
✅ bugfix/dashboard-refresh
✅ docs/update-readme

# Avoid long-lived feature branches
❌ Working on branch for weeks without merging

# Delete merged branches
git branch -d merged-branch
git push origin --delete merged-branch
```

### Before Pushing

```bash
# Checklist:
1. Run tests: ./gradlew test
2. Build APK: ./gradlew assembleDebug
3. Review changes: git diff
4. Check status: git status
5. Commit with good message
6. Pull latest: git pull --rebase origin main
7. Push: git push origin main
```

### Collaboration

```bash
# Pull before you push
git pull origin main
git push origin main

# Use feature branches for collaboration
git checkout -b feature/my-feature
git push -u origin feature/my-feature
# Create Pull Request on GitHub

# Review code before merging
# Use GitHub's review features
# Require approvals for main branch
```

---

## GitHub Actions / CI/CD (Future)

### Example Workflow

Create `.github/workflows/android.yml`:

```yaml
name: Android CI

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v2

    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
        distribution: 'adopt'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

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

## Quick Reference Card

### Essential Commands

```bash
# Status & Info
git status                  # Check status
git log --oneline          # View history
git diff                   # See changes

# Basic Workflow
git add .                  # Stage all
git commit -m "message"    # Commit
git push origin main       # Push
git pull origin main       # Pull

# Branching
git checkout -b branch     # Create branch
git checkout main          # Switch to main
git merge branch           # Merge branch

# Undo
git checkout -- file       # Discard changes
git reset HEAD file        # Unstage
git revert commit          # Revert commit

# Stashing
git stash                  # Save work
git stash pop              # Restore work
```

---

## Project Repository Info

**Repository:** https://github.com/abarata/SmokeReg.git
**Owner:** abarata
**Project:** SmokeReg
**Default Branch:** main
**Current Version:** 1.1

### First Push Command

```bash
git init
git add .
git commit -m "Initial commit - SmokeReg v1.1"
git branch -M main
git remote add origin https://github.com/abarata/SmokeReg.git
git push -u origin main
```

---

## Related Documentation

- [PROJECT_STATUS.md](../PROJECT_STATUS.md) - Current project status
- [CHANGES.md](./CHANGES.md) - Version history
- [BUILD_DEPLOY.md](./BUILD_DEPLOY.md) - Build instructions
- [README.md](../README.md) - Project overview

---

**Documentation Version:** 1.0
**Last Updated:** 2025-10-30
**Status:** Complete
