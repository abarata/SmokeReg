# Future Features & Roadmap

## Overview

This document outlines planned features, enhancements, and improvements for SmokeReg. Features are categorized by priority and complexity.

---

## High Priority Features

### 1. Data Export/Import
**Status:** Planned
**Priority:** High
**Complexity:** Medium

#### Export Functionality
- Export data to JSON file
- Export to CSV format
- Share via Android Share Sheet
- Email export option

#### Import Functionality
- Import from JSON file
- Import from CSV
- Merge with existing data
- Validation of imported data

**Benefits:**
- Data portability
- Backup capability
- Device transfer
- Data analysis in external tools

**Implementation:**
```kotlin
interface DataExporter {
    suspend fun exportToJson(file: File): Boolean
    suspend fun exportToCsv(file: File): Boolean
}

interface DataImporter {
    suspend fun importFromJson(file: File): Result<Int>
    suspend fun importFromCsv(file: File): Result<Int>
}
```

---

### 2. Enhanced Statistics

#### Weekly/Monthly Trends
- Line charts showing smoking patterns
- Bar charts for comparison
- Trend analysis (increasing/decreasing)
- Average per day/week

#### Advanced Metrics
- Longest streak without smoking
- Most productive hours (least smoking)
- Most challenging hours (most smoking)
- Avoidable percentage trends
- Goal tracking

#### Visualization
- Use Jetpack Compose Charts (or MPAndroidChart)
- Interactive graphs
- Date range selection
- Export charts as images

**Benefits:**
- Better insight into habits
- Visual motivation
- Pattern recognition
- Goal achievement tracking

---

### 3. Goals & Targets

#### Goal Setting
- Daily smoke limit
- Weekly reduction target
- Monthly goals
- Custom milestones

#### Progress Tracking
- Real-time progress indicators
- Achievement badges
- Celebration animations
- Streak tracking

#### Notifications
- Warning when approaching daily limit
- Congratulations on achieving goals
- Reminder notifications (optional)

**Benefits:**
- Motivation to reduce smoking
- Clear targets
- Positive reinforcement
- Behavioral change support

---

## Medium Priority Features

### 4. Room Database Migration
**Status:** Planned
**Priority:** Medium
**Complexity:** High

#### Migration Strategy
```kotlin
// Phase 1: Parallel implementation
- Keep existing JSON storage
- Add Room database
- Sync both systems

// Phase 2: Migration
- One-time data migration from JSON to Room
- Fallback to JSON if migration fails

// Phase 3: Deprecation
- Remove JSON storage code
- Use Room exclusively
```

#### Room Schema
```kotlin
@Entity(tableName = "smoke_entries")
data class SmokeEntryEntity(
    @PrimaryKey
    val id: String,
    val dateTime: String,
    val isAvoidable: Boolean,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface SmokeEntryDao {
    @Query("SELECT * FROM smoke_entries ORDER BY dateTime DESC")
    fun getAllEntries(): Flow<List<SmokeEntryEntity>>

    @Query("SELECT * FROM smoke_entries WHERE dateTime LIKE :date || '%'")
    suspend fun getEntriesByDate(date: String): List<SmokeEntryEntity>

    @Insert
    suspend fun insert(entry: SmokeEntryEntity)

    @Update
    suspend fun update(entry: SmokeEntryEntity)

    @Delete
    suspend fun delete(entry: SmokeEntryEntity)
}
```

**Benefits:**
- Better performance with large datasets
- Complex queries support
- Incremental updates
- Type safety
- LiveData/Flow support

---

### 5. Categories & Tags

#### Smoke Categories
- Social (with friends)
- Work break
- Stress relief
- After meals
- Routine/Habit
- Custom categories

#### Tagging System
- Multiple tags per entry
- Filter by category
- Statistics per category
- Identify patterns

**Benefits:**
- Better understanding of triggers
- Targeted reduction strategies
- Category-specific goals
- Pattern analysis

---

### 6. Notes & Context

#### Entry Notes
- Add notes to each entry
- Mood tracking (happy, stressed, neutral)
- Location context
- Activity (driving, walking, etc.)

#### Analysis
- Correlate notes with smoking patterns
- Identify emotional triggers
- Context-aware insights

**Benefits:**
- Deeper self-awareness
- Trigger identification
- Personalized insights
- Therapy support

---

## Low Priority / Nice-to-Have

### 7. Widget Support
- Home screen widget showing today's count
- Quick register button
- Last entry display
- Goal progress widget

### 8. Dark Mode Improvements
- Auto dark mode based on time
- Custom theme colors
- AMOLED black theme
- Accent color customization

### 9. Multi-Language Support
- Internationalization (i18n)
- Spanish, Portuguese, French
- RTL language support
- Locale-based date formats

### 10. Cloud Sync (Optional)
**Note:** Would require internet, contradicts offline-first principle

- Firebase sync (optional feature)
- Multi-device sync
- Account system
- End-to-end encryption

---

## UI/UX Enhancements

### 11. Improved Animations
- Entry add animation
- Delete confirmation animation
- Achievement celebrations
- Smooth transitions
- Micro-interactions

### 12. Better Date Pickers
- Custom Compose date picker
- Material3 date picker (when stable)
- Quick date selection (today, yesterday)
- Calendar view integration

### 13. Swipe Gestures
- Swipe to delete entry
- Swipe to edit
- Pull to refresh
- Gesture customization

### 14. Search & Filter
- Search entries by date
- Filter by avoidable/unavoidable
- Date range selection
- Quick filters (today, this week, etc.)

---

## Technical Improvements

### 15. Dependency Injection
**Current:** Manual DI via Application class
**Future:** Hilt/Dagger

#### Benefits
- Easier testing
- Better separation of concerns
- Scoped dependencies
- Compile-time validation

#### Implementation
```kotlin
@HiltAndroidApp
class SmokeRegApplication : Application()

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideJsonStorageManager(
        @ApplicationContext context: Context
    ): JsonStorageManager = JsonStorageManager(context)

    @Provides
    @Singleton
    fun provideRepository(
        storageManager: JsonStorageManager
    ): SmokeRepository = SmokeRepository(storageManager)
}

@HiltViewModel
class SmokeViewModel @Inject constructor(
    private val repository: SmokeRepository
) : ViewModel()
```

---

### 16. Modularization
- Feature modules
- Core module
- UI module
- Data module

#### Benefits
- Faster build times
- Better separation
- Reusability
- Parallel development

---

### 17. Automated Testing
- Increase unit test coverage to 80%+
- Add integration tests
- UI automation tests
- Screenshot testing
- Performance testing

---

### 18. CI/CD Pipeline
- GitHub Actions / GitLab CI
- Automated builds
- Automated testing
- Lint checks
- Release automation

---

## Data & Analytics

### 19. Health Integration
- Google Fit integration
- Health data export
- Correlation with other health metrics
- Share data with healthcare providers

### 20. AI/ML Insights
- Predict high-risk times
- Personalized recommendations
- Pattern detection
- Anomaly detection

---

## Accessibility Enhancements

### 21. Enhanced Accessibility
- Screen reader optimization
- Voice commands
- Haptic feedback
- High contrast mode
- Large text support
- TalkBack optimization

---

## Performance Optimizations

### 22. Performance Improvements
- LazyColumn optimization
- Image caching (if images added)
- Reduce recompositions
- Background processing
- Memory optimization

---

## Security Features

### 23. Security Enhancements
- Biometric authentication (optional)
- PIN lock
- Private mode
- Data encryption at rest
- Secure backup encryption

---

## Implementation Priority Matrix

### Phase 1 (v1.1) - Essential Features
1. Data Export/Import
2. Enhanced Statistics with Charts
3. Basic Goals & Targets

**Timeline:** 2-3 months

### Phase 2 (v1.2) - Quality of Life
1. Room Database Migration
2. Categories & Tags
3. Notes & Context
4. Widget Support

**Timeline:** 3-4 months

### Phase 3 (v2.0) - Advanced Features
1. Dependency Injection (Hilt)
2. Dark Mode Improvements
3. Advanced Analytics
4. Search & Filter

**Timeline:** 4-6 months

### Phase 4 (v2.1+) - Polish & Extras
1. Multi-Language Support
2. Cloud Sync (optional)
3. Health Integration
4. AI Insights

**Timeline:** Ongoing

---

## Feature Request Process

### User Feedback Integration
1. Collect user feedback via GitHub Issues
2. Prioritize based on demand
3. Evaluate technical feasibility
4. Add to roadmap

### Community Contributions
- Open source model (if applicable)
- Accept pull requests
- Community voting on features

---

## Breaking Changes

### Handling Breaking Changes
- Maintain backward compatibility
- Provide migration tools
- Clear documentation
- Gradual deprecation
- Version the data format

---

## Technical Debt

### Areas to Improve
1. **Error Handling:** More robust error messages
2. **Logging:** Add proper logging system
3. **Testing:** Increase test coverage
4. **Documentation:** Code documentation (KDoc)
5. **Performance:** Profile and optimize

---

## Research & Exploration

### Technologies to Explore
- **Jetpack Compose** - Latest features
- **Kotlin Multiplatform** - iOS support?
- **Compose Destinations** - Type-safe navigation
- **DataStore** - Alternative to JSON
- **WorkManager** - Background tasks

---

## Deprecation Plan

### Features to Eventually Remove
- JSON storage (after Room migration)
- Old Material2 components (if any)
- Deprecated APIs

---

## User-Requested Features

### Common Requests (Placeholder)
_This section will be populated based on user feedback_

Examples:
- Apple Watch integration
- Social features (compare with friends)
- Gamification elements
- Subscription for premium features

---

## Not Planned

### Features We Won't Implement
1. **Social Media Sharing** - Privacy concerns
2. **Public Leaderboards** - Not aligned with goals
3. **Advertisements** - Maintains user experience
4. **Selling User Data** - Privacy-first approach

---

## Related Documentation

- [ARCHITECTURE.md](./ARCHITECTURE.md) - Current architecture
- [DATABASE.md](./DATABASE.md) - Storage details
- [UI.md](./UI.md) - UI components
- [TROUBLESHOOTING.md](./TROUBLESHOOTING.md) - Known issues