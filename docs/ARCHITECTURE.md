# Architecture Guide

## Overview

The Thing You Didn't Touch follows **Clean Architecture** principles with **MVVM** presentation pattern, ensuring separation of concerns, testability, and maintainability.

## Architecture Layers

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐ │
│  │   Screens   │  │  ViewModels │  │   Components    │ │
│  │  (Compose)  │  │   (State)   │  │   (Reusable)    │ │
│  └─────────────┘  └─────────────┘  └─────────────────┘ │
├─────────────────────────────────────────────────────────┤
│                     Domain Layer                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐ │
│  │   Models    │  │  Use Cases  │  │   Interfaces    │ │
│  │ (Entities)  │  │  (Business) │  │ (Repositories)  │ │
│  └─────────────┘  └─────────────┘  └─────────────────┘ │
├─────────────────────────────────────────────────────────┤
│                      Data Layer                          │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────┐ │
│  │ Repositories│  │  DataStore  │  │     Local       │ │
│  │   (Impl)    │  │   (Prefs)   │  │   Persistence   │ │
│  └─────────────┘  └─────────────┘  └─────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

## Layer Details

### Domain Layer

The domain layer contains the core business logic and is independent of any framework.

#### Models

```kotlin
// Core data structures
TouchPoint       // Normalized touch coordinates (0-1)
Heatmap          // Grid-based touch intensity map
SolutionZone     // Target area player must avoid
Level            // Puzzle level configuration
GameState        // Current game session state
UserSettings     // User preferences and progress
```

#### Use Cases

```kotlin
ProcessTouchUseCase    // Handles touch input validation
CheckSolutionUseCase   // Validates puzzle completion
GenerateHintUseCase    // Creates contextual hints
```

### Data Layer

Handles data persistence using Android DataStore.

#### Repositories

```kotlin
GameRepository       // Game state and level management
SettingsRepository   // User preferences and progress
```

#### Local Data Sources

```kotlin
GameStateDataStore   // Persists current game state
SettingsDataStore    // Persists user settings
```

### Presentation Layer

Uses Jetpack Compose with MVVM pattern.

#### Screens

```
MainMenuScreen      // Entry point with navigation
GameScreen          // Core gameplay
SettingsScreen      // User preferences
LevelSelectScreen   // Level selection grid
PrivacyScreen       // Privacy policy display
```

#### ViewModels

```kotlin
GameViewModel       // Manages game state and logic
SettingsViewModel   // Manages settings state
```

## Key Design Decisions

### 1. Normalized Coordinates

All touch coordinates are normalized to 0-1 range:

```kotlin
data class TouchPoint(
    val x: Float,  // 0.0 = left, 1.0 = right
    val y: Float   // 0.0 = top, 1.0 = bottom
)
```

**Benefits:**
- Device-independent positioning
- Consistent behavior across screen sizes
- Easier mathematical operations

### 2. Grid-Based Heatmap

The heatmap uses a configurable grid (default 20x20):

```kotlin
data class Heatmap(
    val gridWidth: Int = 20,
    val gridHeight: Int = 20,
    val cells: List<Float>  // Intensity values 0-1
)
```

**Benefits:**
- Efficient storage and rendering
- Adjustable precision for accessibility
- Fast region calculations

### 3. Immutable State

All state objects are immutable data classes:

```kotlin
fun addTouch(point: TouchPoint): GameState {
    return copy(
        touchPoints = touchPoints + point,
        heatmap = heatmap.addTouch(point)
    )
}
```

**Benefits:**
- Thread safety
- Predictable state changes
- Easy testing

### 4. Sealed Classes for Results

Use cases return sealed classes for type-safe results:

```kotlin
sealed class TouchResult {
    data class Success(val state: GameState) : TouchResult()
    data class SolutionTouched(val state: GameState) : TouchResult()
    data class Ignored(val state: GameState) : TouchResult()
}
```

**Benefits:**
- Exhaustive when expressions
- Clear error handling
- Self-documenting API

## Data Flow

### Touch Input Flow

```
User Touch
    │
    ▼
PuzzleCanvas (Compose)
    │ PointerInput
    ▼
TouchPoint.fromRawCoordinates()
    │ Normalize
    ▼
GameViewModel.processTouch()
    │
    ▼
ProcessTouchUseCase()
    │ Business Logic
    ▼
GameState.addTouch()
    │ Update State
    ▼
GameRepository.saveGameState()
    │ Persist
    ▼
UI Recomposition
```

### Settings Flow

```
User Toggle
    │
    ▼
SettingsScreen (Compose)
    │
    ▼
SettingsViewModel.setSetting()
    │
    ▼
SettingsRepository.update()
    │
    ▼
SettingsDataStore.edit()
    │ Persist
    ▼
Flow<UserSettings>
    │ Emit
    ▼
UI Recomposition
```

## Dependency Injection

Using Hilt for dependency injection:

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideSettingsRepository(
        dataStore: SettingsDataStore
    ): SettingsRepository {
        return SettingsRepository(dataStore)
    }
    
    @Provides
    fun provideProcessTouchUseCase(): ProcessTouchUseCase {
        return ProcessTouchUseCase()
    }
}
```

## Testing Strategy

### Unit Tests

- **Domain Models**: Validation, calculations, transformations
- **Use Cases**: Business logic, edge cases
- **ViewModels**: State management (with mocked repositories)

### Integration Tests

- **Repositories**: DataStore integration
- **Navigation**: Screen transitions

### UI Tests

- **Components**: Composable rendering
- **Interactions**: Touch handling

## Performance Considerations

### Heatmap Rendering

- Only non-zero cells are drawn
- Color calculations are cached
- Grid updates are batched

### State Updates

- Immutable copies minimize side effects
- Flow-based updates trigger targeted recomposition
- Debouncing prevents excessive saves

### Memory Management

- Touch points list is bounded
- Heatmap cells use primitive floats
- No bitmap allocations during gameplay

## Security

### Data Protection

- All data stored locally using DataStore
- No network transmission of gameplay data
- No external analytics or tracking

### Privacy by Design

- Minimal data collection
- No personal identifiers stored
- Optional ad integration with non-personalized ads
