package network.marsys.smarthome.api.models.integration

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.isEqualTo
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import network.marsys.smarthome.api.apiModuleSerializersModule
import network.marsys.smarthome.api.models.integration.IntegrationResponse
import network.marsys.smarthome.domain.identifiers.IntegrationIdentifier

val IntegrationResponseSnapshotTest by testSuite(
    name = "Integration response snapshot tests",
) {
    val json = Json {
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    val identifier = IntegrationIdentifier("test-integration")

    listOf(
        IntegrationResponse.Status.Starting,
        IntegrationResponse.Status.Running,
        IntegrationResponse.Status.Degraded,
        IntegrationResponse.Status.Stopping,
        IntegrationResponse.Status.Stopped,
    ).forEach { status ->
        testSuite(
            name = "Integration response with status: ${status::class.simpleName}",
        ) {
            test(name = "Serializing integration response succeeds") {
                val integration = IntegrationResponse(
                    identifier = identifier,
                    status = status,
                )

                val encoded = json.encodeToString(integration)

                expectThat(encoded)
                    .isEqualTo(
                        """
                            |{
                            |    "identifier": "test-integration",
                            |    "status": {
                            |        "type": "${status::class.simpleName}"
                            |    }
                            |}
                        """.trimMargin(),
                    )
            }

            test(name = "Deserializing integration response succeeds") {
                val encoded = """
                    |{
                    |    "identifier": "test-integration",
                    |    "status": {
                    |        "type": "${status::class.simpleName}"
                    |    }
                    |}
                """.trimMargin()

                val decoded = json.decodeFromString<IntegrationResponse>(encoded)

                expectThat(decoded)
                    .with(IntegrationResponse::identifier) { isEqualTo(identifier) }
                    .with(IntegrationResponse::status) { isEqualTo(status) }
            }
        }
    }

    test(name = "Deserializing integration response fails when identifier is missing") {
        expectThrows<SerializationException> {
            val encoded = """
                |{
                |    "status": {
                |        "type": "Running"
                |    }
                |}
            """.trimMargin()

            json.decodeFromString<IntegrationResponse>(encoded)
        }
    }

    test(name = "Deserializing integration response fails when status is missing") {
        expectThrows<SerializationException> {
            val encoded = """
                |{
                |    "identifier": "test-integration"
                |}
            """.trimMargin()

            json.decodeFromString<IntegrationResponse>(encoded)
        }
    }

    test(name = "Deserializing integration response fails with invalid status") {
        expectThrows<SerializationException> {
            val encoded = """
                |{
                |    "identifier": "test-integration",
                |    "status": {
                |        "type": "Invalid"
                |    }
                |}
            """.trimMargin()

            json.decodeFromString<IntegrationResponse>(encoded)
        }
    }
}
