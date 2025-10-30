# User Interface Documentation

## Overview

SmokeReg uses **Jetpack Compose** with **Material3** design system for a modern, declarative UI. The interface is clean, intuitive, and follows Android design guidelines.

---

## Design Philosophy

### Core Principles
1. **Simplicity** - One-tap smoke registration
2. **Clarity** - Clear visual hierarchy
3. **Consistency** - Material3 design language
4. **Feedback** - Immediate visual feedback for actions
5. **Accessibility** - Clear labels and touch targets

---

## Screens

### 1. Main Screen
**File:** `app/src/main/java/com/smokereg/ui/screens/MainScreen.kt`

**Purpose:** Primary interface for smoke registration and viewing today's entries

#### Layout Structure
```
┌─────────────────────────────────┐
│   TopAppBar: "SmokeReg"         │
├─────────────────────────────────┤
│   Date Selector                 │
│   ┌───────────────────────┐     │
│   │ ◀ Oct 30, 2025  📅 🏠 ▶│     │
│   └───────────────────────┘     │
├─────────────────────────────────┤
│   Registration Card             │
│   ┌───────────────────────┐     │
│   │ ☑ Avoidable checkbox  │     │
│   │                       │     │
│   │ [+ Register Smoke]    │     │
│   │ [🕐 Adjust Time]      │     │
│   └───────────────────────┘     │
├─────────────────────────────────┤
│   Selected Day's Entries        │
│   ┌───────────────────────┐     │
│   │ 14:30  ⚠ Avoidable ✏️ │     │
│   │ 12:15              ✏️ │     │
│   │ 09:00  ⚠ Avoidable ✏️ │     │
│   └───────────────────────┘     │
│                                 │
└─────────────────────────────────┘
```

#### Components

**Top App Bar**
- Title: "SmokeReg"
- Primary color background
- Fixed at top

**Date Selector Card**
- Navigate between different days
- Previous day button (◀)
- Calendar button with current date display
- Today button (appears when not viewing today)
- Next day button (appears when not viewing future dates)
- Material3 secondary container color
- Allows viewing and editing entries from any date

**Registration Card**
- Elevated card with primary container color
- Checkbox for avoidable flag
- Two action buttons (Register, Adjust Time)
- Prominent placement for easy access

**Entries List**
- Scrollable list of selected date's entries
- Most recent at top
- Each entry shows time and avoidable status
- Edit button on each entry

#### States
- **Loading:** Shows CircularProgressIndicator
- **Empty:** "No entries today" message
- **Error:** Error message display
- **Success:** List of entries

---

### 2. Dashboard Screen
**File:** `app/src/main/java/com/smokereg/ui/screens/DashboardScreen.kt`

**Purpose:** Display statistics across different time periods

#### Layout Structure
```
┌─────────────────────────────────┐
│   TopAppBar: "Statistics" 🔄    │
├─────────────────────────────────┤
│   Today                         │
│   ┌───────────────────────┐     │
│   │       5               │     │
│   │     total             │     │
│   │ 2 avoidable (40%)     │     │
│   └───────────────────────┘     │
│                                 │
│   Yesterday                     │
│   ┌───────────────────────┐     │
│   │       3               │     │
│   │     total             │     │
│   │ 1 avoidable (33%)     │     │
│   └───────────────────────┘     │
│                                 │
│   [More stat cards...]          │
│                                 │
│   Insights                      │
│   ┌───────────────────────┐     │
│   │ +2 more than yesterday│     │
│   │ 40% avoidable this week│    │
│   └───────────────────────┘     │
└─────────────────────────────────┘
```

#### Stat Cards
- **Today** - Primary container color
- **Yesterday** - Secondary container color
- **This Week** - Tertiary container color
- **This Month** - Custom green tint
- **This Year** - Custom orange tint

Each card shows:
- Period title
- Large total number
- Avoidable count with percentage
- Color-coded avoidable indicator

---

### 3. Edit Entry Dialog
**File:** `app/src/main/java/com/smokereg/ui/screens/EditEntryDialog.kt`

**Purpose:** Modify or delete existing entry

#### Layout
```
┌─────────────────────────────────┐
│   Edit Entry              [×]   │
├─────────────────────────────────┤
│   Date & Time                   │
│   ┌───────────────────────┐     │
│   │ Oct 28, 2025 14:30    │     │
│   └───────────────────────┘     │
│                                 │
│   [📅 Change Date & Time]       │
│                                 │
│   ☑ Avoidable checkbox          │
│                                 │
│   ──────────────────────        │
│                                 │
│   [🗑️ Delete Entry]             │
│                                 │
├─────────────────────────────────┤
│        [Cancel]   [Save]        │
└─────────────────────────────────┘
```

#### Features
- Date/time picker integration
- Avoidable toggle
- Delete option (red/destructive)
- Save/Cancel buttons

---

### 4. Time Adjust Dialog
**File:** `app/src/main/java/com/smokereg/ui/screens/TimeAdjustDialog.kt`

**Purpose:** Select custom date/time for smoke registration

#### Layout
```
┌─────────────────────────────────┐
│   Select Date and Time    [×]   │
├─────────────────────────────────┤
│        Selected Time            │
│   ┌───────────────────────┐     │
│   │    Oct 28, 2025       │     │
│   │       14:30           │     │
│   └───────────────────────┘     │
│                                 │
│   [📅 Change Date]              │
│   [🕐 Change Time]              │
│                                 │
│   ──────────────────────        │
│                                 │
│   ☑ Avoidable checkbox          │
│                                 │
├─────────────────────────────────┤
│    [Cancel] [Register Smoke]    │
└─────────────────────────────────┘
```

---

## Components

### 1. SmokeEntryCard
**File:** `app/src/main/java/com/smokereg/ui/components/SmokeEntryCard.kt`

**Purpose:** Reusable card for displaying single entry

#### Structure
```kotlin
@Composable
fun SmokeEntryCard(
    entry: SmokeEntry,
    onEditClick: (SmokeEntry) -> Unit,
    modifier: Modifier = Modifier
)
```

#### Visual Elements
- Time display (HH:mm format)
- Avoidable badge (if applicable)
- Edit button
- Card elevation
- Rounded corners

#### States
- Normal: White/Surface color
- Avoidable: Orange warning badge

---

### 2. StatCard
**File:** `app/src/main/java/com/smokereg/ui/components/StatCard.kt`

**Purpose:** Display statistics for a time period

#### Structure
```kotlin
@Composable
fun StatCard(
    title: String,
    total: Int,
    avoidable: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
)
```

#### Visual Elements
- Period title
- Large total number
- Avoidable count with percentage
- Color-coded layout
- Card elevation

---

### 3. BottomNavBar
**File:** `app/src/main/java/com/smokereg/ui/components/BottomNavBar.kt`

**Purpose:** Navigate between main sections

#### Structure
```kotlin
@Composable
fun BottomNavBar(
    navController: NavController,
    modifier: Modifier = Modifier
)
```

#### Items
1. **Home** - MainScreen (House icon)
2. **Dashboard** - DashboardScreen (Chart icon)

#### Behavior
- Active item highlighted
- Filled icons when selected
- Outlined icons when unselected
- Smooth transitions

---

## Material3 Theme

### Theme File
**Location:** `app/src/main/java/com/smokereg/ui/theme/Theme.kt`

### Color Scheme

#### Light Theme
```kotlin
primary = Color(0xFF1976D2)         // Blue
onPrimary = Color(0xFFFFFFFF)       // White
primaryContainer = Color(0xFFD1E4FF) // Light Blue
secondary = Color(0xFFFF6F00)       // Orange
tertiary = Color(0xFF7B1FA2)        // Purple
background = Color(0xFFFDFCFF)      // Off-White
surface = Color(0xFFFDFCFF)         // Off-White
```

#### Dark Theme
```kotlin
primary = Color(0xFF90CAF9)         // Light Blue
onPrimary = Color(0xFF003258)       // Dark Blue
primaryContainer = Color(0xFF004881) // Medium Blue
secondary = Color(0xFFFFB74D)       // Light Orange
tertiary = Color(0xFFE1BEE7)        // Light Purple
background = Color(0xFF1A1C1E)      // Dark Gray
surface = Color(0xFF1A1C1E)         // Dark Gray
```

### Typography
**Location:** `app/src/main/java/com/smokereg/ui/theme/Typography.kt`

Uses Material3 typography scale:
- **Display:** Large headers
- **Headline:** Section titles
- **Title:** Card titles
- **Body:** Regular text
- **Label:** Small labels

---

## Design Tokens

### Spacing
```kotlin
Spacer(modifier = Modifier.height(8.dp))   // Small
Spacer(modifier = Modifier.height(16.dp))  // Medium
Spacer(modifier = Modifier.height(24.dp))  // Large
```

### Padding
```kotlin
.padding(4.dp)      // Minimal
.padding(8.dp)      // Small
.padding(12.dp)     // Medium
.padding(16.dp)     // Standard
.padding(24.dp)     // Large
```

### Elevation
```kotlin
elevation = CardDefaults.cardElevation(
    defaultElevation = 2.dp   // Normal cards
    defaultElevation = 4.dp   // Emphasized cards
)
```

### Corner Radius
```kotlin
RoundedCornerShape(4.dp)    // Small
RoundedCornerShape(8.dp)    // Medium
RoundedCornerShape(12.dp)   // Large
```

---

## User Interactions

### Touch Targets
- Minimum 48dp × 48dp (Material Design standard)
- Buttons: Full width or adequate spacing
- Icons: 40dp × 40dp clickable area

### Feedback

#### Visual Feedback
- Ripple effect on buttons
- State changes (pressed, focused)
- Color transitions

#### Toast Messages
- Success: "Smoke registered"
- Update: "Entry updated"
- Delete: "Entry deleted"
- Error: Specific error message

#### Loading States
- CircularProgressIndicator for async operations
- Disabled buttons during processing

---

## Animations

### Screen Transitions
- Slide animation between screens
- Fade animation for dialogs
- Bottom sheet behavior for dialogs

### List Animations
- Smooth scrolling
- Item animations on add/remove (potential future feature)

### State Changes
- Smooth checkbox animations
- Button press animations
- Icon transitions (filled ↔ outlined)

---

## Accessibility

### Content Descriptions
```kotlin
Icon(
    imageVector = Icons.Default.Edit,
    contentDescription = "Edit",  // Screen reader support
    tint = MaterialTheme.colorScheme.primary
)
```

### Labels
- All buttons have text labels
- Icons paired with text where possible
- Clear, descriptive text

### Touch Targets
- All interactive elements meet minimum size
- Adequate spacing between targets

### Contrast
- Material3 ensures sufficient contrast
- Colors chosen for readability

---

## Responsive Design

### Portrait Mode (Primary)
- Optimized for single-column layout
- Scrollable content
- Bottom navigation

### Landscape Mode
- Same layout adapts to wider screen
- Content fills width appropriately
- Bottom nav remains accessible

---

## State Management in UI

### Composable State
```kotlin
@Composable
fun MainScreen(viewModelFactory: SmokeViewModelFactory) {
    val viewModel: SmokeViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsState()
    val todaysEntries by viewModel.todaysEntries.collectAsState()

    // UI recomposes when state changes
}
```

### State Hoisting
- State managed in ViewModels
- UI is stateless
- Callbacks for user actions

---

## Common UI Patterns

### Loading Pattern
```kotlin
if (uiState.isLoading) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
```

### Empty State Pattern
```kotlin
if (entries.isEmpty()) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_entries_today),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
```

### Error Pattern
```kotlin
if (uiState.error != null) {
    Text(
        text = uiState.error,
        color = MaterialTheme.colorScheme.error
    )
}
```

---

## Best Practices

### Do's ✅
- Use Material3 components
- Follow theme colors
- Provide content descriptions
- Handle all states (loading, empty, error, success)
- Use StateFlow for reactive UI
- Hoist state to ViewModels

### Don'ts ❌
- Don't hardcode colors (use theme)
- Don't ignore accessibility
- Don't perform business logic in Composables
- Don't use deprecated Material2 components
- Don't create state in Composables (hoist it)

---

## Testing UI

### Preview Functions
```kotlin
@Preview(showBackground = true)
@Composable
fun SmokeEntryCardPreview() {
    SmokeRegTheme {
        SmokeEntryCard(
            entry = SmokeEntry(
                id = "preview",
                dateTime = "2025-10-28T14:30:00",
                isAvoidable = true
            ),
            onEditClick = {}
        )
    }
}
```

### UI Tests
Location: `app/src/androidTest/`
- Test user flows
- Verify UI elements exist
- Test interactions

---

## Future UI Enhancements

See [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) for planned improvements:
- Dark mode toggle
- Custom themes
- Charts and graphs
- Animations
- Swipe gestures

---

## Related Documentation

- [NAVIGATION.md](./NAVIGATION.md) - Screen navigation details
- [ARCHITECTURE.md](./ARCHITECTURE.md) - UI layer architecture
- [FUTURE_FEATURES.md](./FUTURE_FEATURES.md) - Planned UI enhancements