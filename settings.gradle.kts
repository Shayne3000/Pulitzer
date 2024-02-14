@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Pulitzer"
include(":app")
include(":core:database")
include(":core:ui")
include(":core:test")
include(":data:article")
include(":domain:articles")
include(":domain:bookmarks")
include(":feature:home")
include(":feature:bookmarks")
