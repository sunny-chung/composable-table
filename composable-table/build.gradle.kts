plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}

android {
    namespace = "com.sunnychung.lib.android.composabletable"
    compileSdk = 34

    defaultConfig {
        minSdk = 23
    }
    buildTypes {
        release {
            isMinifyEnabled = false
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
    implementation("androidx.compose.ui:ui:${libs.versions.jetpack.compose.get()}")
    implementation("androidx.compose.foundation:foundation:${libs.versions.jetpack.compose.get()}")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.sunny-chung"
            artifactId = "composable-table"
            version = "1.0.0"
        }
    }
}
