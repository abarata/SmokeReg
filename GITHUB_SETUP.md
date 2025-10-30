# GitHub Push Setup Guide

**Repository:** https://github.com/abarata/SmokeReg.git
**Status:** ✅ Git initialized and committed, ready to push

---

## Current Status

✅ Git repository initialized
✅ Remote added: origin → https://github.com/abarata/SmokeReg.git
✅ All files committed (62 files, 10,912 lines)
✅ Branch: main
✅ Ready to push

❌ **Authentication Required** - Choose one method below

---

## Authentication Options

### Option 1: Personal Access Token (Recommended)

This is the most secure and recommended method for HTTPS authentication.

#### Step 1: Create Personal Access Token on GitHub

1. Go to GitHub: https://github.com/settings/tokens
2. Click **"Generate new token"** → **"Generate new token (classic)"**
3. Give it a name: `SmokeReg Development`
4. Set expiration: 90 days (or as needed)
5. Select scopes:
   - ✅ `repo` (Full control of private repositories)
   - ✅ `workflow` (if using GitHub Actions)
6. Click **"Generate token"**
7. **COPY THE TOKEN** - You won't see it again!

#### Step 2: Configure Git to Use Token

**Option A: Store in Git Credential Manager (Recommended)**
```bash
# Configure Git to cache credentials
git config --global credential.helper store

# Now push (you'll be prompted once)
git push -u origin main

# When prompted:
# Username: abarata
# Password: <paste your personal access token>

# Future pushes will use cached credentials
```

**Option B: Use Token Directly in URL**
```bash
# Push with token in URL (one-time)
git push https://abarata:<YOUR_TOKEN>@github.com/abarata/SmokeReg.git main

# Then set URL without token for future
git remote set-url origin https://github.com/abarata/SmokeReg.git
```

**Option C: Environment Variable**
```bash
# Set token as environment variable
export GITHUB_TOKEN=<your_token>

# Use Git credential helper
echo "https://abarata:${GITHUB_TOKEN}@github.com" > ~/.git-credentials
git config --global credential.helper store

# Push
git push -u origin main
```

---

### Option 2: SSH Key (Most Secure)

Better for long-term use and automation.

#### Step 1: Generate SSH Key

```bash
# Generate SSH key (if you don't have one)
ssh-keygen -t ed25519 -C "abarata@webwise.pt"

# When prompted:
# - File location: Press Enter (default: ~/.ssh/id_ed25519)
# - Passphrase: Enter a secure passphrase (or leave empty)

# Start SSH agent
eval "$(ssh-agent -s)"

# Add key to agent
ssh-add ~/.ssh/id_ed25519

# Copy public key to clipboard
cat ~/.ssh/id_ed25519.pub
# Copy the output
```

#### Step 2: Add SSH Key to GitHub

1. Go to: https://github.com/settings/keys
2. Click **"New SSH key"**
3. Title: `WSL Development Machine`
4. Key type: `Authentication Key`
5. Paste the public key you copied
6. Click **"Add SSH key"**

#### Step 3: Change Remote URL to SSH

```bash
# Change remote from HTTPS to SSH
git remote set-url origin git@github.com:abarata/SmokeReg.git

# Verify
git remote -v
# Should show:
# origin  git@github.com:abarata/SmokeReg.git (fetch)
# origin  git@github.com:abarata/SmokeReg.git (push)

# Test SSH connection
ssh -T git@github.com
# Should see: "Hi abarata! You've successfully authenticated..."

# Push
git push -u origin main
```

---

### Option 3: GitHub CLI (Easiest)

Simplest method if you have GitHub CLI installed.

#### Install GitHub CLI

```bash
# Install gh (if not installed)
# For Ubuntu/Debian
curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg | sudo dd of=/usr/share/keyrings/githubcli-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | sudo tee /etc/apt/sources.list.d/github-cli.list > /dev/null
sudo apt update
sudo apt install gh
```

#### Authenticate and Push

```bash
# Login to GitHub
gh auth login

# Follow prompts:
# - What account: GitHub.com
# - Protocol: HTTPS
# - Authenticate: Login with a web browser
# - Copy the one-time code and press Enter
# - Browser will open, paste code and authorize

# Configure Git to use GitHub CLI
gh auth setup-git

# Push
git push -u origin main
```

---

## Quick Command Reference

### After Authentication is Set Up

```bash
# Check remote
git remote -v

# Check branch
git branch

# Push to GitHub
git push -u origin main

# Future pushes (after first push)
git push

# Pull from GitHub
git pull origin main
```

### View Commit Log

```bash
# See commit history
git log --oneline

# See detailed commit
git show
```

### Tag the Release

```bash
# Create version tag
git tag -a v1.1 -m "Version 1.1 - Initial release with date navigation"

# Push tag
git push origin v1.1

# View tags
git tag -l
```

---

## Verification Steps

After successful push:

1. **Check GitHub Repository**
   - Visit: https://github.com/abarata/SmokeReg
   - Verify all files are present
   - Check commit message

2. **Verify Local Status**
```bash
git status
# Should show: "Your branch is up to date with 'origin/main'"

git log --oneline
# Should show your commit
```

3. **Create GitHub Release (Optional)**
   - Go to: https://github.com/abarata/SmokeReg/releases
   - Click **"Create a new release"**
   - Tag: `v1.1`
   - Title: `SmokeReg v1.1`
   - Description: Copy from CHANGES.md
   - Attach `SmokeReg-debug.apk` file
   - Click **"Publish release"**

---

## Troubleshooting

### "Authentication failed"
```bash
# If using token, verify it's correct
# If using SSH, test connection:
ssh -T git@github.com

# Re-authenticate
gh auth login  # if using GitHub CLI
```

### "Permission denied"
```bash
# Check you have write access to the repository
# Verify repository exists: https://github.com/abarata/SmokeReg

# If repository doesn't exist, create it on GitHub first:
# Go to https://github.com/new
# Repository name: SmokeReg
# Description: Android smoking tracker app
# Public or Private: Your choice
# Don't initialize with README (we already have one)
# Click "Create repository"
```

### "Repository not found"
```bash
# Verify repository URL
git remote -v

# If wrong, update it
git remote set-url origin https://github.com/abarata/SmokeReg.git
```

### "Would clobber existing tag"
```bash
# If tag already exists
git tag -d v1.1  # delete local
git push origin :refs/tags/v1.1  # delete remote
git tag -a v1.1 -m "Version 1.1"  # recreate
git push origin v1.1  # push new tag
```

---

## Repository Structure on GitHub

After push, your GitHub repository will have:

```
SmokeReg/
├── README.md                    # Main project overview
├── PROJECT_STATUS.md            # Current status
├── GITHUB_SETUP.md             # This file
├── .gitignore                   # Git ignore rules
├── _documentation/              # Complete documentation (12 files)
├── app/                         # Android app source
├── gradle/                      # Gradle wrapper
├── build.gradle.kts            # Root build config
├── settings.gradle.kts         # Project settings
├── gradlew                      # Gradle wrapper script
└── build_apk.sh                # Build script
```

**NOT included (correctly):**
- ❌ `build/` folders
- ❌ `local.properties`
- ❌ `.gradle/` cache
- ❌ `.idea/` (except minimal files)
- ❌ `*.apk` files

---

## Next Steps After Successful Push

1. **Verify on GitHub**
   - Check all files are visible
   - Verify README renders correctly
   - Check documentation folder

2. **Set up Branch Protection (Optional)**
   - Go to: Settings → Branches
   - Add rule for `main` branch
   - Require pull request reviews
   - Require status checks

3. **Create Release (Recommended)**
   - Tag: v1.1
   - Upload APK
   - Add release notes from CHANGES.md

4. **Update Repository Settings**
   - Add description
   - Add topics: android, kotlin, jetpack-compose, smoking-tracker
   - Add website (if any)

5. **Configure GitHub Actions (Future)**
   - See GIT_WORKFLOW.md for CI/CD setup
   - Auto-build on push
   - Run tests automatically

---

## Summary

**Current State:**
- ✅ Repository initialized
- ✅ All files committed
- ✅ Remote configured
- ⏳ **Waiting for push authentication**

**To Complete:**
1. Choose authentication method above (Token, SSH, or GitHub CLI)
2. Follow the steps for your chosen method
3. Run: `git push -u origin main`
4. Verify on GitHub: https://github.com/abarata/SmokeReg

**Questions?**
- Check [GIT_WORKFLOW.md](_documentation/GIT_WORKFLOW.md) for detailed Git operations
- Check GitHub documentation: https://docs.github.com/en/authentication

---

**Ready to Push:** Yes ✅
**Command:** `git push -u origin main` (after authentication setup)
**Repository:** https://github.com/abarata/SmokeReg.git
