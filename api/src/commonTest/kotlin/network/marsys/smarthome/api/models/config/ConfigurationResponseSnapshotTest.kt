package network.marsys.smarthome.api.models.config

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.isEqualTo
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import network.marsys.smarthome.api.apiModuleSerializersModule

val ConfigurationResponseSnapshotTest by testSuite(
    name = "Configuration response snapshot tests",
) {
    val json = Json {
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing configuration response succeeds") {
        val response = ConfigurationResponse(
            baseUri = "https://smarthome.com/",
        )

        val encoded = json.encodeToString(response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "baseUri": "https://smarthome.com/"
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing configuration response succeeds") {
        val encoded = """
            |{
            |    "baseUri": "https://smarthome.com/"
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<ConfigurationResponse>(encoded)

        expectThat(decoded)
            .with(ConfigurationResponse::baseUri) { isEqualTo("https://smarthome.com/") }
    }

    test(name = "Deserializing configuration response fails when baseUri is missing") {
        expectThrows<SerializationException> {
            val encoded = """
                |{
                |}
            """.trimMargin()

            json.decodeFromString<ConfigurationResponse>(encoded)
        }
    }
}
