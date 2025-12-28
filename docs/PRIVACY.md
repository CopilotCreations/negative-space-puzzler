# Privacy Policy

**Last Updated: December 2024**

## Summary

"The Thing You Didn't Touch" is designed with privacy as a core principle. **Your gameplay data stays on your device.** We do not collect, transmit, or share any personal information.

## Data Collection

### What We Do NOT Collect

- ❌ Personal information (name, email, phone)
- ❌ Location data
- ❌ Device identifiers
- ❌ Gameplay analytics
- ❌ Touch heatmaps or patterns
- ❌ Usage statistics
- ❌ Crash reports (unless explicitly enabled)

### What We Store Locally

The following data is stored **only on your device**:

| Data Type | Purpose | Location |
|-----------|---------|----------|
| Game Progress | Resume gameplay | DataStore |
| High Scores | Track achievements | DataStore |
| Settings | User preferences | DataStore |
| Current Level | Save/load state | DataStore |

## Touch Tracking Explanation

### How It Works

The game tracks your touch interactions **solely for gameplay purposes**:

1. **Touch Detection**: When you tap the screen, coordinates are captured
2. **Normalization**: Coordinates are converted to 0-1 range (device-independent)
3. **Heatmap Generation**: Touch intensity is visualized locally
4. **Solution Checking**: Your touches are compared against hidden solution zones

### Data Lifecycle

```
Touch Event → Normalize → Store in Memory → Display Heatmap
                                          ↓
                               Level Complete/Reset → Clear
```

### What Happens to Touch Data

- **During Gameplay**: Stored in memory only
- **On Level Complete**: Cleared from memory
- **On Level Reset**: Cleared from memory
- **On App Close**: Not persisted (fresh start on reopen)
- **Never**: Transmitted to any server

## Third-Party Services

### Advertising (Optional)

We use **Google AdMob** for optional, non-intrusive banner ads:

- Ads are **clearly labeled**
- You can **disable ads** in Settings
- We use **non-personalized ads** by default
- AdMob may collect limited device information per Google's policies

**Google Privacy Policy**: https://policies.google.com/privacy

### To Disable Ads

1. Open the app
2. Go to Settings
3. Toggle "Show Ads" off

## Data Storage Security

### Local Storage

All data is stored using **Android DataStore**, which provides:

- Encrypted storage
- App-private access
- Automatic corruption handling

### No Cloud Sync

- No data is uploaded to cloud services
- No account required
- No cross-device synchronization

## Children's Privacy (COPPA)

- This app does **not** knowingly collect information from children under 13
- The app is suitable for **all ages**
- No in-app purchases targeting children
- No social features or chat

## Your Rights

### GDPR (European Union)

| Right | How We Comply |
|-------|---------------|
| Access | All data is on your device - you have full access |
| Rectification | Modify settings anytime in the app |
| Erasure | Uninstall the app to delete all data |
| Portability | Use Android backup to export app data |
| Object | No data processing to object to |

### CCPA (California)

| Right | How We Comply |
|-------|---------------|
| Know | We don't collect personal information |
| Delete | Uninstall or clear app data |
| Opt-Out | No data sale to opt out of |
| Non-Discrimination | No data-based services to discriminate |

### How to Exercise Your Rights

Since all data is local:

1. **View Data**: Check Settings for all stored information
2. **Delete Data**: Settings → Apps → The Thing You Didn't Touch → Clear Data
3. **Complete Removal**: Uninstall the application

## Data Retention

| Data Type | Retention Period |
|-----------|-----------------|
| Game Progress | Until app uninstall |
| Settings | Until app uninstall |
| Touch Data | Current session only |
| Heatmaps | Current session only |

## Permissions

The app requests minimal permissions:

| Permission | Purpose | Required |
|------------|---------|----------|
| INTERNET | Load ads (if enabled) | Optional |
| ACCESS_NETWORK_STATE | Check connectivity for ads | Optional |
| VIBRATE | Haptic feedback (if enabled) | Optional |

**No sensitive permissions are requested or required.**

## Security Measures

1. **Local-Only Storage**: No server-side data
2. **Encrypted Preferences**: DataStore encryption
3. **No External SDKs**: Minimal dependencies
4. **Regular Updates**: Security patches as needed

## Changes to This Policy

We may update this policy:

- Changes will be noted with new "Last Updated" date
- Material changes will be highlighted in app updates
- Continued use constitutes acceptance

## Contact Information

For privacy-related inquiries:

- **Email**: privacy@negativespace.puzzler.example.com
- **Response Time**: Within 30 days

## Compliance Summary

| Regulation | Status |
|------------|--------|
| GDPR | ✅ Compliant |
| CCPA | ✅ Compliant |
| COPPA | ✅ Compliant |
| LGPD (Brazil) | ✅ Compliant |
| PIPEDA (Canada) | ✅ Compliant |
| APP (Australia) | ✅ Compliant |

## Technical Details

### Data Minimization

We follow the principle of data minimization:

```
What we could collect:     What we actually collect:
├── Device ID              ├── (nothing)
├── Location               │
├── Usage patterns         │
├── Touch coordinates      │
├── Session duration       │
├── User demographics      │
└── Advertising ID         └──
```

### Privacy by Design

The application was built with privacy considerations from the start:

1. **Requirement**: Track touches for gameplay
2. **Solution**: In-memory only, normalized, cleared on reset
3. **Verification**: Code review confirms no data transmission

---

*This privacy policy is written in plain language to be easily understood. If you have questions about any section, please contact us.*
