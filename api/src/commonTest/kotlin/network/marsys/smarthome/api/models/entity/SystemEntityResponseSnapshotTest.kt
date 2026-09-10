package network.marsys.smarthome.api.models.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.hasMessageStartingWith
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isNotNull
import dev.nmarsman.expect.assertions.isNull
import kotlinx.serialization.json.Json
import network.marsys.smarthome.api.apiModuleSerializersModule
import kotlin.time.Clock
import kotlin.time.Instant

val SystemEntityResponseSnapshotTest by testSuite(
    name = "System entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    val system = SystemEntity(
        identifier = "system.smarthome",
        state = SystemEntity.State(
            host = SystemEntity.Host(
                device = SystemEntity.Device(
                    manufacturer = "Raspberry",
                    model = "Raspberry 5",
                    architecture = "arm64",
                    physicalCores = 4,
                    logicalCores = 8,
                ),
                operatingSystem = SystemEntity.OperatingSystem(
                    description = "Raspbian OS",
                    family = "Linux",
                    version = "11.0",
                    bitness = 64,
                ),
            ),
            processor = SystemEntity.Processor(
                load = .15f,
                temperature = 55f,
            ),
            memory = SystemEntity.Memory(
                total = 8f,
                available = 4f,
                swap = SystemEntity.Swap(
                    total = 2f,
                    used = 1f,
                ),
            ),
            SystemEntity.Uptime(
                host = Instant.parse("2026-09-01T00:00:00Z"),
                application = Instant.parse("2026-09-01T00:01:00Z"),
            ),
        ),
    )

    test(name = "Serializing system entity response succeeds with all attributes present") {
        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = system)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "system",
                    |    "identifier": "system.smarthome",
                    |    "state": {
                    |        "host": {
                    |            "device": {
                    |                "manufacturer": "Raspberry",
                    |                "model": "Raspberry 5",
                    |                "architecture": "arm64",
                    |                "physicalCores": 4,
                    |                "logicalCores": 8
                    |            },
                    |            "os": {
                    |                "description": "Raspbian OS",
                    |                "family": "Linux",
                    |                "version": "11.0",
                    |                "bitness": 64
                    |            }
                    |        },
                    |        "processor": {
                    |            "load": 0.15,
                    |            "temperature": 55.0
                    |        },
                    |        "memory": {
                    |            "total": 8.0,
                    |            "available": 4.0,
                    |            "swap": {
                    |                "total": 2.0,
                    |                "used": 1.0
                    |            }
                    |        },
                    |        "uptime": {
                    |            "host": "2026-09-01T00:00:00Z",
                    |            "application": "2026-09-01T00:01:00Z"
                    |        }
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing system entity response succeeds with optional attributes missing") {
        val encoded = json.encodeToString(
            serializer = EntityResponse.serializer(),
            value = system.copy(
                state = system.state?.copy(
                    host = system.state.host.copy(
                        operatingSystem = system.state.host.operatingSystem.copy(
                            version = null,
                        ),
                    ),
                    processor = system.state.processor.copy(
                        temperature = null,
                    ),
                ),
            ),
        )

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "system",
                    |    "identifier": "system.smarthome",
                    |    "state": {
                    |        "host": {
                    |            "device": {
                    |                "manufacturer": "Raspberry",
                    |                "model": "Raspberry 5",
                    |                "architecture": "arm64",
                    |                "physicalCores": 4,
                    |                "logicalCores": 8
                    |            },
                    |            "os": {
                    |                "description": "Raspbian OS",
                    |                "family": "Linux",
                    |                "bitness": 64
                    |            }
                    |        },
                    |        "processor": {
                    |            "load": 0.15
                    |        },
                    |        "memory": {
                    |            "total": 8.0,
                    |            "available": 4.0,
                    |            "swap": {
                    |                "total": 2.0,
                    |                "used": 1.0
                    |            }
                    |        },
                    |        "uptime": {
                    |            "host": "2026-09-01T00:00:00Z",
                    |            "application": "2026-09-01T00:01:00Z"
                    |        }
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing system entity response succeeds with unknown state") {
        val response = SystemEntity(
            identifier = "system.smarthome",
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "system",
                    |    "identifier": "system.smarthome"
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing system entity response fails with missing identifier") {
        expectThrows<IllegalArgumentException> {
            val encoded = """
                |{
                |    "type": "system",
                |}
            """.trimMargin()

            json.decodeFromString<EntityResponse>(string = encoded)
        }
    }

    test(name = "Deserializing system entity response fails with missing required state") {
        expectThrows<IllegalArgumentException> {
            val encoded = """
                |{
                |    "type": "system",
                |    "identifier": "system.smarthome",
                |    "state": {
                |        "host": {
                |            "os": {
                |                "version": "11.0"
                |            }
                |        },
                |        "processor": {
                |            "temperature": 55.0
                |        }
                |    }
                |}
            """.trimMargin()

            json.decodeFromString<EntityResponse>(string = encoded)
        }
    }
}
