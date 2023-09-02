plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.sunnychung.lib.android.composabletable.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sunnychung.lib.android.composabletable.demo"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
}

dependencies {
    implementation(project(":composable-table"))
    implementation("androidx.compose.ui:ui:${libs.versions.jetpack.compose.get()}")
    implementation("androidx.compose.foundation:foundation:${libs.versions.jetpack.compose.get()}")
    implementation("androidx.compose.material:material:${libs.versions.jetpack.compose.get()}")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    api("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
}
