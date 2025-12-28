# Usage Guide

## Getting Started

### First Launch

When you first open "The Thing You Didn't Touch," you'll be greeted by the main menu:

1. **Start Game** - Begin from level 1 (Tutorial)
2. **Select Level** - Choose any unlocked level
3. **Settings** - Configure accessibility and preferences
4. **Privacy** - Read the privacy policy

### Tutorial (Level 0)

The tutorial teaches you the core mechanics:

1. **Touch anywhere** on the canvas to explore
2. **Watch the heatmap** show where you've touched
3. **Cover enough area** (progress bar fills up)
4. **Avoid the solution zone** - it's hidden!
5. **Reveal the solution** when ready

## Gameplay

### Objective

Find the hidden solution zone by process of elimination. You must cover enough of the canvas WITHOUT touching the solution zone.

### Controls

#### Standard Mode
- **Tap** anywhere on the canvas to mark that area
- Multiple taps accumulate intensity (shown as heatmap)

#### Alternative Input Mode
- **Swipe** across the canvas to mark areas
- Useful for motor accessibility

#### Reduced Precision Mode
- Larger touch targets
- Bigger grid cells
- More forgiving hit detection

### Game HUD

The heads-up display shows:

| Element | Description |
|---------|-------------|
| Level Name | Current puzzle name and description |
| Touches | Number of times you've tapped |
| Coverage | Percentage of canvas explored |
| Time | Elapsed time since level start |
| Difficulty | Easy/Normal/Hard/Expert badge |
| Progress Bar | Visual coverage progress |
| Reveal Button | Becomes active at required coverage |

### Winning and Losing

#### Victory Conditions
1. Reach the minimum coverage requirement
2. Click "Reveal Solution"
3. The solution zone must be untouched

#### Failure Conditions
- Touch any point inside the solution zone
- Solution is revealed and you've touched it

### Scoring

Your score is calculated based on:

| Factor | Contribution |
|--------|--------------|
| Base Score | 1000 points |
| Coverage Bonus | Up to 500 points (more coverage = more points) |
| Efficiency Bonus | Up to 300 points (fewer touches = more points) |
| Difficulty Multiplier | Easy: 1x, Normal: 1.5x, Hard: 2x, Expert: 3x |

## Levels

### Level Progression

| Level | Name | Difficulty | Notes |
|-------|------|------------|-------|
| 0 | Tutorial | Easy | Learn the basics |
| 1 | Corner Secret | Easy | Solution in a corner |
| 2 | Edge Walker | Easy | Solution at the edge |
| 3 | Circle of Mystery | Normal | Elliptical solution zone |
| 4 | The Thin Line | Normal | Narrow vertical zone |
| 5 | Bottom Dweller | Normal | Solution at bottom |
| 6 | Tiny Target | Hard | Small solution zone |
| 7 | Top Secret | Hard | Ellipse at top |
| 8 | The Wide Path | Hard | Wide horizontal zone |
| 9 | Corner Master | Expert | Corner solution |
| 10 | Final Challenge | Expert | Timed challenge |

### Unlocking Levels

- Complete a level to unlock the next
- Level select shows locked/unlocked status
- Stars indicate completed levels

## Settings

### Accessibility Options

#### Reduced Precision Mode
- **Effect**: Larger touch targets, 10x10 grid (vs 20x20)
- **Use for**: Motor impairments, shaky hands

#### Alternative Input Mode
- **Effect**: Swipe gestures instead of taps
- **Use for**: Difficulty with precise taps

#### High Contrast Mode
- **Effect**: Black/white/yellow color scheme
- **Use for**: Visual impairments, color blindness

#### Haptic Feedback
- **Effect**: Vibration on each touch
- **Use for**: Audio-off environments, additional feedback

### Other Settings

#### Sound Effects
- Toggle game sounds on/off

#### Show Ads
- Enable/disable ad banner
- Support development by keeping ads on

## Tips and Strategies

### General Tips

1. **Start at the edges** - Solution zones rarely span the entire canvas
2. **Work systematically** - Cover area in a pattern
3. **Watch the coverage** - Don't over-touch; efficiency matters
4. **Use hints wisely** - Levels include hint text

### Advanced Strategies

1. **Guess early** - If you think you know where the solution is, avoid it
2. **Boundary mapping** - Touch around suspected areas to confirm
3. **Time management** - Expert levels may have time limits
4. **Score optimization** - Minimize touches for higher scores

## Troubleshooting

### Game Won't Start

1. Ensure Android 7.0+ (API 24+)
2. Clear app cache
3. Reinstall the app

### Touch Not Registering

1. Enable "Reduced Precision Mode"
2. Clean your screen
3. Try "Alternative Input Mode"

### Progress Lost

Game state is saved automatically. If progress is lost:
1. Check app permissions
2. Don't clear app data
3. Report bug with device info

### Ads Not Showing

Ads require internet connection. If disabled in settings, enable them to support development.

## Privacy

### What We Track

**Nothing.** The game tracks:
- ❌ No personal information
- ❌ No analytics
- ❌ No gameplay data sent to servers
- ❌ No device identifiers

### What's Stored Locally

- Game progress (current level, high scores)
- Settings preferences
- Current game state (for pause/resume)

### Clearing Data

To remove all data:
1. Go to Android Settings
2. Apps → The Thing You Didn't Touch
3. Clear Data

Or simply uninstall the app.

## Support

### Reporting Bugs

Include:
- Device model
- Android version
- Steps to reproduce
- Screenshots if possible

### Feature Requests

We welcome suggestions for:
- New level ideas
- Accessibility improvements
- UI enhancements

## FAQ

**Q: Can I play offline?**
A: Yes! The game works completely offline.

**Q: How do I disable ads?**
A: Settings → Support → Show Ads → Off

**Q: Is my data backed up?**
A: Local data only. Use Android backup if desired.

**Q: How many levels are there?**
A: Currently 11 (including tutorial), with more planned.

**Q: What's the highest score possible?**
A: Theoretical maximum is ~5400 on Expert levels with perfect efficiency.
