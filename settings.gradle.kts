@file:Suppress("UnstableApiUsage")

include(":core:datastore")


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
include(":core:model")
include(":core:network")
include(":core:ui")
include(":core:test")
include(":data:articles")
include(":domain:articles")
include(":feature:home")
include(":feature:details")
include(":feature:bookmarks")
