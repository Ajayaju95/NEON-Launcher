plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.neonlauncher.home"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.neonlauncher.home"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
