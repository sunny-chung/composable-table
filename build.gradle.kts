// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    val androidGradlePluginVersion = "8.1.0"
    val kotlinVersion = "1.8.21"

    id("com.android.application") version androidGradlePluginVersion apply false
    id("com.android.library") version androidGradlePluginVersion apply false
    kotlin("android") version kotlinVersion apply false
    kotlin("plugin.serialization") version kotlinVersion apply false
}
