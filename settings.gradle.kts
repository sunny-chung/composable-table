pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// https://stackoverflow.com/questions/60534770/exception-when-building-a-kotlin-js-project-error-package-json-name-contains-i
rootProject.name = "ComposableTable"

include(":composable-table")
include(":demo:android-only-app")
include(":demo:multiplatform-app")
project(":demo:multiplatform-app").projectDir = File("$rootDir/demo/multiplatform-app/shared")
