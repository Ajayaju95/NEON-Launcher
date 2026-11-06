# NEON Video Edition

Native Android HOME launcher based on the supplied reference video.

## Screens
- Screen 1: minimalist weekday heading and vertical app list.
- Screen 2: oversized date, focus/search graphic, time/battery area and shortcut list.
- Screen 3: circular radial clock with digital time and date.

## Gestures
- Swipe left/right to move between the three launcher screens.
- Swipe vertically on the first screen to scroll the app list.
- Tap an app label to launch the matching installed app.

## Build
The GitHub Actions workflow builds a debug APK automatically on pushes to `main` and manual dispatch. Open the repository's **Actions** tab and download the `NEON-Video-Edition-debug` artifact from a successful run.

The build uses JDK 17, Gradle 8.9, Android Gradle Plugin 8.7.3 and Android SDK 35.

## Install on Samsung Galaxy S24 Ultra
1. Download and extract the workflow artifact ZIP.
2. Install `app-debug.apk` on the phone (allow installation from the browser/files app when Android prompts).
3. Press Home and select **NEON Video Edition** as the default Home app.

## Notes
- This is a visual and interaction recreation, not a pixel-perfect extraction of the original launcher APK.
- The coastline background is drawn as a lightweight Canvas illustration; the original wallpaper file was not supplied separately.
- App glyphs are simple typographic marks. App taps resolve against installed launcher activities by app name.
- Weather, media session, calendar and actual battery percentage are not wired to live providers in this initial build.
- Android's lock screen, system navigation and notification shade remain controlled by One UI.
