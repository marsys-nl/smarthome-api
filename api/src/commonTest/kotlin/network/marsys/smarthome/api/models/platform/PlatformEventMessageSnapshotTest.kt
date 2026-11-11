package network.marsys.smarthome.api.models.platform

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.hasMessage
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import network.marsys.smarthome.api.apiModuleSerializersModule

val PlatformEventMessageSnapshotTest by testSuite(
    name = "Platform event message snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serialize capability updated event message succeeds - string") {
        val message = PlatformEventMessage.CapabilityUpdated(
            identifier = "light.living-room",
            capability = CapabilityMessage.EnumValue(
                type = "thermostatMode",
                context = ContextMessage(elements = emptyList()),
                current = "heat",
            ),
            occurredAt = "2024-06-01T12:00:00Z",
        )

        val encoded = json.encodeToString(PlatformEventMessage.serializer(), message)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "capability-updated",
                    |    "identifier": "light.living-room",
                    |    "capability": {
                    |        "type": "thermostatMode",
                    |        "context": {},
                    |        "current": "heat"
                    |    },
                    |    "occurredAt": "2024-06-01T12:00:00Z"
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serialize capability updated event message succeeds - boolean") {
        val message = PlatformEventMessage.CapabilityUpdated(
            identifier = "light.living-room",
            capability = CapabilityMessage.BooleanValue(
                type = "onOff",
                context = ContextMessage(elements = emptyList()),
                current = true,
            ),
            occurredAt = "2024-06-01T12:00:00Z",
        )

        val encoded = json.encodeToString(PlatformEventMessage.serializer(), message)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "capability-updated",
                    |    "identifier": "light.living-room",
                    |    "capability": {
                    |        "type": "onOff",
                    |        "context": {},
                    |        "current": true
                    |    },
                    |    "occurredAt": "2024-06-01T12:00:00Z"
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serialize capability updated event message succeeds - double") {
        val message = PlatformEventMessage.CapabilityUpdated(
            identifier = "light.living-room",
            capability = CapabilityMessage.DoubleValue(
                type = "measureTemperature",
                context = ContextMessage(
                    elements = listOf(
                        ContextElementMessage.TemperatureType.Current,
                    ),
                ),
                current = 37.5,
            ),
            occurredAt = "2024-06-01T12:00:00Z",
        )

        val encoded = json.encodeToString(PlatformEventMessage.serializer(), message)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "capability-updated",
                    |    "identifier": "light.living-room",
                    |    "capability": {
                    |        "type": "measureTemperature",
                    |        "context": {
                    |            "elements": [
                    |                "current-temperature-type"
                    |            ]
                    |        },
                    |        "current": 37.5
                    |    },
                    |    "occurredAt": "2024-06-01T12:00:00Z"
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserialize capability updated event message succeeds - string") {
        val encoded = """
            |{
            |    "type": "capability-updated",
            |    "identifier": "light.living-room",
            |    "capability": {
            |        "type": "measureLoad",
            |        "context": {},
            |        "current": "heat"
            |    },
            |    "occurredAt": "2024-06-01T12:00:00Z"
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<PlatformEventMessage>(string = encoded)

        expectThat(decoded)
            .isA<PlatformEventMessage.CapabilityUpdated>()
            .get(PlatformEventMessage.CapabilityUpdated::capability)
            .isA<CapabilityMessage.EnumValue>()
    }

    test(name = "Deserialize capability updated event message succeeds - boolean") {
        val encoded = """
            |{
            |    "type": "capability-updated",
            |    "identifier": "light.living-room",
            |    "capability": {
            |        "type": "onOff",
            |        "context": {},
            |        "current": true
            |    },
            |    "occurredAt": "2024-06-01T12:00:00Z"
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<PlatformEventMessage>(string = encoded)

        expectThat(decoded)
            .isA<PlatformEventMessage.CapabilityUpdated>()
            .get(PlatformEventMessage.CapabilityUpdated::capability)
            .isA<CapabilityMessage.BooleanValue>()
    }

    test(name = "Deserialize capability updated event message succeeds - double") {
        val encoded = """
            |{
            |    "type": "capability-updated",
            |    "identifier": "light.living-room",
            |    "capability": {
            |        "type": "measureLoad",
            |        "context": {},
            |        "current": 50.0
            |    },
            |    "occurredAt": "2024-06-01T12:00:00Z"
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<PlatformEventMessage>(string = encoded)

        expectThat(decoded)
            .isA<PlatformEventMessage.CapabilityUpdated>()
            .get(PlatformEventMessage.CapabilityUpdated::capability)
            .isA<CapabilityMessage.DoubleValue>()
    }

    test(name = "Deserialize capability updated event message succeeds - int") {
        val encoded = """
            |{
            |    "type": "capability-updated",
            |    "identifier": "light.living-room",
            |    "capability": {
            |        "type": "measureLoad",
            |        "context": {},
            |        "current": 50
            |    },
            |    "occurredAt": "2024-06-01T12:00:00Z"
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<PlatformEventMessage>(string = encoded)

        expectThat(decoded)
            .isA<PlatformEventMessage.CapabilityUpdated>()
            .get(PlatformEventMessage.CapabilityUpdated::capability)
            .isA<CapabilityMessage.DoubleValue>()
    }

    test(name = "Deserialize capability updated event message succeeds - float") {
        val encoded = """
            |{
            |    "type": "capability-updated",
            |    "identifier": "light.living-room",
            |    "capability": {
            |        "type": "measureLoad",
            |        "context": {},
            |        "current": 3.14f
            |    },
            |    "occurredAt": "2024-06-01T12:00:00Z"
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<PlatformEventMessage>(string = encoded)

        expectThat(decoded)
            .isA<PlatformEventMessage.CapabilityUpdated>()
            .get(PlatformEventMessage.CapabilityUpdated::capability)
            .isA<CapabilityMessage.DoubleValue>()
    }

    test(name = "Deserialize capability updated event message fails if current value is missing") {
        val encoded = """
            |{
            |    "type": "capability-updated",
            |    "identifier": "light.living-room",
            |    "capability": {
            |        "type": "onOff",
            |        "context": {}
            |    },
            |    "occurredAt": "2024-06-01T12:00:00Z"
            |}
        """.trimMargin()

        expectThrows<SerializationException> { json.decodeFromString<PlatformEventMessage>(string = encoded) }
            .hasMessage("Missing or invalid capability current value")
    }

    test(name = "Deserialize capability updated event message fails if current value is an object") {
        val encoded = """
            |{
            |    "type": "capability-updated",
            |    "identifier": "light.living-room",
            |    "capability": {
            |        "type": "onOff",
            |        "context": {},
            |        "current": {}
            |    },
            |    "occurredAt": "2024-06-01T12:00:00Z"
            |}
        """.trimMargin()

        expectThrows<SerializationException> { json.decodeFromString<PlatformEventMessage>(string = encoded) }
            .hasMessage("Missing or invalid capability current value")
    }

    test(name = "Deserialize capability updated event message fails if capability is not an object") {
        val encoded = """
            |{
            |    "type": "capability-updated",
            |    "identifier": "light.living-room",
            |    "capability": true,
            |    "occurredAt": "2024-06-01T12:00:00Z"
            |}
        """.trimMargin()

        expectThrows<SerializationException> { json.decodeFromString<PlatformEventMessage>(string = encoded) }
            .hasMessage("Missing or invalid capability current value")
    }

    test(name = "Deserialize capability updated event message fails if current is unsupported type") {
        val encoded = """
            |{
            |    "type": "capability-updated",
            |    "identifier": "light.living-room",
            |    "capability": {
            |        "type": "measureLoad",
            |        "context": {},
            |        "current": null
            |    },
            |    "occurredAt": "2024-06-01T12:00:00Z"
            |}
        """.trimMargin()

        expectThrows<SerializationException> { json.decodeFromString<PlatformEventMessage>(string = encoded) }
            .hasMessage("Unsupported capability value: null")
    }
}
