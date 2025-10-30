# Git Repository Rules

**Repository:** https://github.com/abarata/SmokeReg
**Last Updated:** 2025-10-30

---

## ⚠️ CRITICAL RULE - MUST READ ⚠️

```
╔═══════════════════════════════════════════════════════════════════════════╗
║                                                                           ║
║                                                                           ║
║  🚨🚨🚨  NEVER, NEVER and NEVER do a sync/commit/push  🚨🚨🚨           ║
║         without being EXPLICITLY ASKED by the project owner!              ║
║                                                                           ║
║                                                                           ║
║  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ║
║                                                                           ║
║  ✋ ALL Git operations must be EXPLICITLY REQUESTED by the user:          ║
║                                                                           ║
║     • git commit     ❌ NEVER without explicit request                    ║
║     • git push       ❌ NEVER without explicit request                    ║
║     • git pull       ❌ NEVER without explicit request                    ║
║     • git merge      ❌ NEVER without explicit request                    ║
║     • git fetch      ❌ NEVER without explicit request                    ║
║     • git rebase     ❌ NEVER without explicit request                    ║
║                                                                           ║
║  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ║
║                                                                           ║
║  👤 It will ALWAYS be the USER who asks to:                               ║
║     • Commit changes                                                      ║
║     • Push to GitHub                                                      ║
║     • Pull from GitHub                                                    ║
║     • Sync the repository                                                 ║
║     • Merge branches                                                      ║
║     • Create tags or releases                                             ║
║                                                                           ║
║  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ║
║                                                                           ║
║  ❌ DO NOT commit automatically                                           ║
║  ❌ DO NOT push automatically                                             ║
║  ❌ DO NOT sync automatically                                             ║
║  ❌ DO NOT assume changes should be committed                             ║
║  ❌ DO NOT suggest committing unless asked                                ║
║  ❌ DO NOT proactively offer to push                                      ║
║                                                                           ║
║  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ║
║                                                                           ║
║  ✅ ONLY perform Git operations when explicitly instructed                ║
║  ✅ Wait for clear user request like:                                     ║
║     "commit this"                                                         ║
║     "push to github"                                                      ║
║     "sync the repository"                                                 ║
║     "update github"                                                       ║
║                                                                           ║
║  ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━  ║
║                                                                           ║
║                                                                           ║
║                  This is a VERY IMPORTANT RULE!                           ║
║                                                                           ║
║                      Violating this rule is                               ║
║                        UNACCEPTABLE!                                      ║
║                                                                           ║
║                                                                           ║
╚═══════════════════════════════════════════════════════════════════════════╝
```

---

## Who This Rule Applies To

This rule applies to **EVERYONE and EVERYTHING** working on this project:

- ✋ **AI Assistants** (Claude, GitHub Copilot, etc.)
- ✋ **Automated Tools** (scripts, bots, automation)
- ✋ **CI/CD Pipelines** (GitHub Actions, Jenkins, etc.)
- ✋ **Contributors** (if this project accepts contributions)
- ✋ **Team Members** (if working in a team)

**NO EXCEPTIONS!**

---

## Allowed Git Operations (Read-Only)

These operations are **ALLOWED** because they only read information and don't modify the repository:

✅ `git status` - Check repository status
✅ `git log` - View commit history
✅ `git diff` - View changes
✅ `git show` - View commit details
✅ `git branch` - List branches (without creating)
✅ `git remote -v` - View remote URLs

**These are READ-ONLY and safe to use.**

---

## Forbidden Git Operations (Write Operations)

These operations are **FORBIDDEN** without explicit user request:

❌ `git commit` - Creating commits
❌ `git push` - Pushing to remote
❌ `git pull` - Pulling from remote
❌ `git merge` - Merging branches
❌ `git fetch` - Fetching from remote
❌ `git rebase` - Rebasing commits
❌ `git tag` - Creating tags
❌ `git checkout -b` - Creating new branches
❌ `git add` - Staging changes (without commit intent)
❌ `git reset` - Resetting commits
❌ `git revert` - Reverting commits
❌ `git cherry-pick` - Cherry-picking commits

**NEVER execute these without explicit user permission!**

---

## Correct Workflow

### ❌ WRONG - Automatic Commit

```
User: "I fixed the bug in the dashboard"
AI: "Great! I'll commit and push those changes now..."
     git add .
     git commit -m "Fix dashboard bug"
     git push origin main

🚨 THIS IS WRONG! NEVER DO THIS!
```

### ✅ CORRECT - Wait for Permission

```
User: "I fixed the bug in the dashboard"
AI: "Great work! The fix looks good."

User: "Commit and push this to GitHub"
AI: "I'll commit and push the changes now."
     git add .
     git commit -m "Fix dashboard bug"
     git push origin main

✅ THIS IS CORRECT! User explicitly requested the Git operations.
```

---

## Examples of Explicit Requests

User requests that **ALLOW** Git operations:

✅ "Commit this"
✅ "Commit these changes"
✅ "Push to GitHub"
✅ "Sync with GitHub"
✅ "Update the repository"
✅ "Push the changes"
✅ "Commit and push"
✅ "Merge this branch"
✅ "Pull from GitHub"
✅ "Create a commit"

User statements that **DO NOT** allow Git operations:

❌ "I made changes" (just information)
❌ "The code is ready" (no commit request)
❌ "Everything works now" (no sync request)
❌ "I'm done" (no explicit Git request)
❌ "Looks good" (no commit instruction)

**When in doubt, ask the user:**
"Would you like me to commit/push these changes to GitHub?"

---

## Rationale

This rule exists because:

1. **User Control** - The project owner wants full control over what goes into the repository
2. **Review Process** - User wants to review changes before they're committed
3. **Commit Quality** - User wants to ensure proper commit messages and organization
4. **Intentionality** - Each commit should be intentional, not automatic
5. **Trust** - Following this rule builds trust with AI assistants and tools

---

## Enforcement

If this rule is violated:

1. ⚠️ **First time**: Warning and reminder of the rule
2. 🚫 **Repeated violation**: Loss of trust and confidence
3. ❌ **Severe violation**: Relationship with AI assistant terminated

**This rule is non-negotiable.**

---

## Questions?

If you're unsure whether something requires permission:

1. **Default to asking**: "Would you like me to commit/push these changes?"
2. **Check for explicit request**: Did the user clearly ask for a Git operation?
3. **When in doubt, DON'T**: Better to ask than to violate the rule

---

## Summary

```
┌─────────────────────────────────────────────────────────┐
│                                                         │
│  NEVER commit/push/pull/merge without being asked!      │
│                                                         │
│  ALWAYS wait for explicit user permission!              │
│                                                         │
│  This is the #1 most important rule for this project!  │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

---

## Related Documentation

- **[GIT_WORKFLOW.md](_documentation/GIT_WORKFLOW.md)** - Complete Git workflow guide
- **[GITHUB_SETUP.md](GITHUB_SETUP.md)** - GitHub authentication setup
- **[PROJECT_STATUS.md](PROJECT_STATUS.md)** - Current project status
- **[README.md](README.md)** - Project overview

---

**This document must be read and understood by anyone working on this project.**

**Last Updated:** 2025-10-30
**Status:** MANDATORY
**Priority:** CRITICAL
