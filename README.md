# The Thing You Didn't Touch

An innovative Android puzzle game where the solution is based on the areas you **never** interacted with. Built with Jetpack Compose and following modern Android development practices.

## 🎮 Game Concept

The core idea is simple yet unique: instead of finding and touching something, you must **avoid** touching the hidden solution zone. The puzzle reveals itself through negative space - the areas left unexplored.

### How It Works

1. **Touch the canvas** to explore and mark areas
2. **Cover enough area** to reveal where the solution might be
3. **Avoid the solution zone** - if you touch it, you lose
4. **Reveal the answer** when you've covered enough territory

## ✨ Features

- **10+ Unique Levels** with increasing difficulty
- **Touch Heatmap Visualization** showing your exploration patterns
- **Multiple Solution Zone Shapes** (rectangles and ellipses)
- **Accessibility Features**:
  - Reduced Precision Mode (larger touch targets)
  - Alternative Input Mode (swipe gestures)
  - High Contrast Mode
  - Haptic Feedback
- **Privacy-First Design** - all data stays on device
- **Non-Intrusive Ads** - clearly labeled and optional

## 🛠️ Technical Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Local Storage**: DataStore
- **Testing**: JUnit, MockK, Turbine

## 📁 Project Structure

```
app/
├── src/main/java/com/negativespace/puzzler/
│   ├── data/
│   │   ├── local/         # DataStore implementations
│   │   └── repository/    # Repository implementations
│   ├── di/                # Hilt dependency injection
│   ├── domain/
│   │   ├── model/         # Core domain models
│   │   └── usecase/       # Business logic use cases
│   ├── ui/
│   │   ├── components/    # Reusable UI components
│   │   ├── screens/       # Screen composables & ViewModels
│   │   └── theme/         # Material theme
│   ├── util/              # Utility classes
│   ├── MainActivity.kt
│   └── NegativeSpacePuzzlerApp.kt
├── src/test/              # Unit tests
└── src/androidTest/       # Instrumented tests
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34

### Build Instructions

1. Clone the repository
```bash
git clone https://github.com/yourusername/negative-space-puzzler.git
cd negative-space-puzzler
```

2. Open in Android Studio

3. Sync Gradle files

4. Run on emulator or device
```bash
./gradlew installDebug
```

### Build Debug APK

```bash
./gradlew assembleDebug
```

The APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

### Run Tests

```bash
# Unit tests
./gradlew testDebugUnitTest

# With coverage report
./gradlew jacocoTestReport

# Instrumented tests (requires device/emulator)
./gradlew connectedDebugAndroidTest
```

## 📊 Test Coverage

The project maintains **75%+ code coverage** on core domain logic:

| Module | Coverage |
|--------|----------|
| domain/model | 90%+ |
| domain/usecase | 85%+ |
| data/repository | 75%+ |

## 🔒 Privacy

This app is designed with privacy as a core principle:

- **No data collection** - zero analytics or tracking
- **Local storage only** - all data stays on device
- **No network calls** except for optional ads
- **GDPR/CCPA compliant** - no personal data processed

See [Privacy Policy](docs/PRIVACY.md) for complete details.

## 📱 Accessibility

The game includes multiple accessibility features:

- **Reduced Precision Mode**: Larger touch targets and grid cells
- **Alternative Input Mode**: Swipe gestures instead of taps
- **High Contrast Mode**: Maximum contrast colors
- **Haptic Feedback**: Configurable vibration
- **Screen Reader Support**: Content descriptions for all elements

## 🎯 Roadmap

- [ ] Additional level packs
- [ ] Daily challenges
- [ ] Achievements system
- [ ] Leaderboards (local)
- [ ] Custom level creator

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

Contributions are welcome! Please read our contributing guidelines before submitting PRs.

## 📚 Documentation

- [Architecture Guide](docs/ARCHITECTURE.md)
- [Usage Guide](docs/USAGE.md)
- [Suggestions for Improvement](docs/SUGGESTIONS.md)
