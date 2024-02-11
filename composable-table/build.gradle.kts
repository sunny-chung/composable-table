import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("maven-publish")
    id("com.android.library")
//    kotlin("android")
}

kotlin {
    jvm(name = "desktop") {
        jvmToolchain(17)
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {

    }

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    val iosTargets = listOf(
        iosArm64(),
        iosSimulatorArm64(),
        iosX64(),
    )

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
        }
    }
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
    buildFeatures {
        compose = true
    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.4.7"
//    }
}

dependencies {
//    implementation("androidx.compose.ui:ui:${libs.versions.jetpack.compose.get()}")
//    implementation("androidx.compose.foundation:foundation:${libs.versions.jetpack.compose.get()}")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.sunny-chung"
            artifactId = "composable-table"
            version = "1.1.0"

            afterEvaluate {
//                from(components["release"])
            }
        }
    }
}
