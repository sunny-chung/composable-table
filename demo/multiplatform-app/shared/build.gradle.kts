import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.application")

    kotlin("plugin.serialization")
}

kotlin {
    jvm(name = "desktop") {
        jvmToolchain(17)
    }

    @OptIn(ExperimentalWasmDsl::class)
    val wasmTarget = wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
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
    ).apply {
        forEach {
            it.binaries.framework {
                baseName = "ComposableTableDemoShared"
                isStatic = true
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation(compose.components.resources)

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

            implementation(project(":composable-table"))
            }
        }

        val commonNonAndroidMain by creating {
            dependsOn(commonMain)
        }

        androidMain.dependencies {
            implementation("androidx.activity:activity-compose:1.7.2")
        }

        val desktopMain by getting {
            dependsOn(commonNonAndroidMain)
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        val iosMain by creating {
            dependsOn(commonNonAndroidMain)
        }
        configure(iosTargets) {
            compilations["main"].defaultSourceSet.dependsOn(iosMain)
        }

        wasmTarget.compilations["main"].defaultSourceSet.dependsOn(commonNonAndroidMain)
    }
}

android {
    namespace = "com.sunnychung.lib.android.composabletable.mpdemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sunnychung.lib.android.composabletable.mpdemo"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
    }
    sourceSets["main"].res.srcDirs("src/commonMain/composeResources")
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.9"
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Composable Table Demo"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}
