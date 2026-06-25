rootProject.name = "morphe-patches"

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/brosssh/instagram-morphe-patches-library")
            credentials {
                username = providers.gradleProperty("gpr.user").getOrElse(System.getenv("GITHUB_ACTOR"))
                password = providers.gradleProperty("gpr.key").getOrElse(System.getenv("GITHUB_TOKEN"))
            }
        }
    }
}

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/MorpheApp/registry")
            credentials {
                username = providers.gradleProperty("gpr.user").getOrElse(System.getenv("GITHUB_ACTOR"))
                password = providers.gradleProperty("gpr.key").getOrElse(System.getenv("GITHUB_TOKEN"))
            }
        }
        maven { url = uri("https://jitpack.io") }
    }
}

plugins {
    id("app.morphe.patches") version "1.3.2-dev.2"
}

// Include instagram-morphe-patches-library as a composite build if it exists locally
mapOf(
    "libs/instagram-morphe-patches-library" to mapOf(
        "app.morphe:instagram-morphe-patches-library" to ":patch-library",
        "app.morphe:instagram-morphe-extensions-library" to ":extension-library",
    ),
).forEach { (libraryPath, substitutions) ->
    val libDir = file(libraryPath)
    if (libDir.exists()) {
        includeBuild(libDir) {
            dependencySubstitution {
                substitutions.forEach { (libraryName, projectPath) ->
                    substitute(module(libraryName)).using(project(projectPath))
                }
            }
        }
    }
}

settings {
    extensions {
        defaultNamespace = "app.morphe.extension"

        // Must resolve to an absolute path (not relative),
        // otherwise the extensions in subfolders will fail to find the proguard config.
        proguardFiles(rootProject.projectDir.resolve("extensions/proguard-rules.pro").toString())
    }
}

