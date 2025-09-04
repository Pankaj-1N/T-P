pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("com.android.application") version "8.5.2"
        id("com.android.library") version "8.5.2"
        id("com.google.dagger.hilt.android") version "2.52"
        kotlin("android") version "1.9.24"
        kotlin("kapt") version "1.9.24"

        // Add the dependency for the Google services Gradle plugin
        id("com.google.gms.google-services") version "4.4.3" apply false

    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "T&P"
include(":app")
