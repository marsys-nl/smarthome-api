import java.net.URI

rootProject.name = "smarthome-api"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()

        maven {
            name = "Central Portal Snapshots"
            url = URI.create("https://central.sonatype.com/repository/maven-snapshots/")

            mavenContent {
                snapshotsOnly()
            }

            content {
                includeModuleByRegex("network\\.marsys\\.smarthome", "smarthome-.*")
            }
        }
    }
}

include(":api")
