package network.marsys.smarthome.api.models.platform

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.hasMessage
import dev.nmarsman.expect.assertions.isEqualTo
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import network.marsys.smarthome.api.apiModuleSerializersModule

val ContextMessageSnapshotTest by testSuite(
    name = "Context message snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        serializersModule = apiModuleSerializersModule
    }

    val elements = mapOf(
        ContextElementMessage.MemoryType.Total to "total-memory-type",
        ContextElementMessage.MemoryType.Available to "available-memory-type",
        ContextElementMessage.MemoryType.SwapTotal to "swap-total-memory-type",
        ContextElementMessage.MemoryType.SwapAvailable to "swap-available-memory-type",
        ContextElementMessage.TemperatureType.Current to "current-temperature-type",
        ContextElementMessage.TemperatureType.Outdoor to "outdoor-temperature-type",
    )

    testSuite(name = "Serialize context elements") {
        elements.forEach { (element, serialName) ->
            test(name = "Serialize context element succeeds - $serialName") {
                val context = ContextMessage(listOf(element))
                val encoded = json.encodeToString(ContextMessage.serializer(), context)

                expectThat(encoded)
                    .isEqualTo("""{"elements":["$serialName"]}""")
            }
        }

        test(name = "Serialize context message with multiple elements succeeds") {
            val context = ContextMessage(
                elements = listOf(
                    ContextElementMessage.TemperatureType.Current,
                    ContextElementMessage.MemoryType.Total,
                ),
            )

            val encoded = json.encodeToString(ContextMessage.serializer(), context)

            expectThat(encoded)
                .isEqualTo("""{"elements":["current-temperature-type","total-memory-type"]}""")
        }

        test(name = "Serialize empty context message succeeds") {
            val encoded = json.encodeToString(ContextMessage.serializer(), ContextMessage())

            expectThat(encoded)
                .isEqualTo("{}")
        }
    }

    testSuite(name = "Deerialize context elements") {
        elements.forEach { (element, serialName) ->
            test(name = "Deserialize context element succeeds - $serialName") {
                val context = ContextMessage(listOf(element))
                val decoded = json.decodeFromString(ContextMessage.serializer(), """{"elements":["$serialName"]}""")

                expectThat(decoded)
                    .isEqualTo(context)
            }
        }

        test(name = "Deserialize empty context message succeeds") {
            val decoded = json.decodeFromString(ContextMessage.serializer(), "{}")

            expectThat(decoded)
                .isEqualTo(ContextMessage())
        }

        test(name = "Deserialize context element fails if serial name is unknown") {
            expectThrows<SerializationException> {
                json.decodeFromString(ContextMessage.serializer(), """{"elements":["unknown-type"]}""")
            }
        }

        mapOf(
            "object" to """{"type":"current-temperature-type"}""",
            "array" to """["current-temperature-type"]""",
            "number" to "42",
            "boolean" to "true",
            "null" to "null",
        ).forEach { (kind, element) ->
            test(name = "Deserialize context element fails if element is $kind") {
                expectThrows<SerializationException> {
                    json.decodeFromString(ContextMessage.serializer(), """{"elements":[$element]}""")
                }.hasMessage("Unsupported type: $element")
            }
        }
    }
}
