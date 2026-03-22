plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

group = "network.marsys.smarthome"
version = libs.versions.smarthome.api.get()

kotlin {
    jvmToolchain(libs.versions.jvm.toolchain.get().toInt())

    applyDefaultHierarchyTemplate()

    jvm()

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "network.marsys.smarthome",
        artifactId = "smarthome-api",
        version = libs.versions.smarthome.api.get(),
    )

    pom {
        name.set("Smarthome - API")
        description.set("API models for Smarthome system, used by both the app and the hub in order to communicate")
        inceptionYear.set("2025")

        organization {
            name.set("Marsys")
            url.set("https://www.marsys.network")
        }

        developers {
            developer {
                id.set("nmrsmn")
                name.set("Niels Marsman")
                email.set("niels.marsman@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/marsys-nl/smarthome-api")
            connection.set("git@github.com:marsys-nl/smarthome-api.git")
        }

        licenses {
            license {
                name = "The MIT License (MIT)"
                url = "https://mit-license.org/"
                distribution = "https://mit-license.org/"
            }
        }
    }
}
