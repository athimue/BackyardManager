pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "Backyard"
include(":app")
include(":core:database")
include(":core:theme")
include(":feature:countdown:api")
include(":feature:countdown:impl:domain")
include(":feature:countdown:impl:ui")
include(":feature:race:api")
include(":feature:race:impl:ui")
include(":feature:race:impl:data")
include(":feature:race:impl:domain")