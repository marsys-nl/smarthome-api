package network.marsys.smarthome.api.models.config

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.isEqualTo
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import network.marsys.smarthome.api.apiModuleSerializersModule

val HealthResponseSnapshotTest by testSuite(
    name = "Health response snapshot tests",
) {
    val json = Json {
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing health response succeeds") {
        val response = HealthResponse(
            app = "network.marsys.smarthome",
            version = "2026.08",
        )

        val encoded = json.encodeToString(response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "app": "network.marsys.smarthome",
                    |    "version": "2026.08"
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing health response succeeds") {
        val encoded = """
            |{
            |    "app": "network.marsys.smarthome",
            |    "version": "2026.08"
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<HealthResponse>(encoded)

        expectThat(decoded)
            .with(HealthResponse::app) { isEqualTo("network.marsys.smarthome") }
            .with(HealthResponse::version) { isEqualTo("2026.08") }
    }

    test(name = "Deserializing health response fails when app is missing") {
        expectThrows<SerializationException> {
            val encoded = """
                |{
                |    "version": "2026.05"
                |}
            """.trimMargin()

            json.decodeFromString<HealthResponse>(encoded)
        }
    }

    test(name = "Deserializing health response fails when version is missing") {
        expectThrows<SerializationException> {
            val encoded = """
                |{
                |    "app": "network.marsys.smarthome"
                |}
            """.trimMargin()

            json.decodeFromString<HealthResponse>(encoded)
        }
    }
}
