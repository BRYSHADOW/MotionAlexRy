# Motion Simulator

Ung dung Android mo phong chuyen dong giua hai diem, xay dung bang Jetpack Compose + Material 3.

## Build Status
[![Build Android APK](https://github.com/YOUR_USERNAME/MotionSimulator/actions/workflows/build.yml/badge.svg)](https://github.com/YOUR_USERNAME/MotionSimulator/actions/workflows/build.yml)

## Tinh nang
- Cham Do / Cham Xanh co the keo tha tu do
- Hieu ung glow, bong do, phong to khi keo
- Chuyen dong muot 60FPS voi noi suy Linear hoac Ease In/Out
- Chinh toc do (0.1x – 5x) va delay (0 – 5 giay)
- Hien thi toa do X/Y theo thoi gian thuc
- Che do Lap vo han
- Dark Mode + 5 mau chu de
- Rung / Am thanh phan hoi

## Yeu cau
- Android 8.0+ (API 26)
- Android Studio Ladybug (2024.2.x) tro len

## Cach build

### Debug APK (tu dong qua GitHub Actions)
1. Push code len GitHub
2. Vao tab **Actions** → chon workflow **Build Android APK**
3. Click job da chay → cuon xuong **Artifacts** → tai `MotionSimulator-debug-*`

### Release APK
Push mot tag bat dau bang `v`:
```bash
git tag v1.0.0
git push origin v1.0.0
```
GitHub Actions tu dong build va tao GitHub Release.

### Build thu cong
```bash
# Debug
./gradlew assembleDebug

# Release (can ky)
./gradlew assembleRelease
```

## Cau truc
```
app/src/main/java/com/example/motionsimulator/
├── MainActivity.kt
├── data/
│   ├── model/AppSettings.kt
│   └── repository/SettingsRepository.kt
├── ui/
│   ├── theme/         (Color, Type, Theme)
│   ├── navigation/    (Screen, AppNavigation)
│   ├── components/    (SimulationCanvas, ControlPanel, CoordinateDisplay)
│   └── screens/
│       ├── simulation/ (SimulationViewModel, SimulationScreen)
│       └── settings/   (SettingsViewModel, SettingsScreen)
└── utils/             (AnimationUtils, VibrationUtils, SoundUtils)
```

## Cong nghe
Kotlin · Jetpack Compose · Material 3 · MVVM · Coroutines · StateFlow · DataStore · Navigation Compose
