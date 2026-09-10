package network.marsys.smarthome.api.models.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isNotNull
import dev.nmarsman.expect.assertions.isNull
import kotlinx.serialization.json.Json
import network.marsys.smarthome.api.apiModuleSerializersModule

val LightEntityResponseSnapshotTest by testSuite(
    name = "Light entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing light entity response succeeds with all attributes present") {
        val response = LightEntity(
            identifier = "light.living-room",
            state = LightEntity.State(
                onOff = true,
                brightness = .5f,
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "light",
                    |    "identifier": "light.living-room",
                    |    "state": {
                    |        "onOff": true,
                    |        "brightness": 0.5
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing light entity response succeeds with optional attributes missing") {
        val response = LightEntity(
            identifier = "light.living-room",
            state = LightEntity.State(
                onOff = true,
                brightness = null,
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "light",
                    |    "identifier": "light.living-room",
                    |    "state": {
                    |        "onOff": true
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing light entity response succeeds with unknown state") {
        val response = LightEntity(
            identifier = "light.living-room",
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "light",
                    |    "identifier": "light.living-room"
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing light entity response succeeds with all attributes present") {
        val encoded = """
            |{
            |    "type": "light",
            |    "identifier": "light.living-room",
            |    "state": {
            |        "onOff": true,
            |        "brightness": 0.5
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .with(EntityResponse::identifier) { isEqualTo("light.living-room") }
            .with(EntityResponse::state) {
                this
                    .isNotNull()
                    .isA<LightEntity.State>()
                    .with(LightEntity.State::onOff) { isEqualTo(true) }
                    .with(LightEntity.State::brightness) { isEqualTo(0.5f) }
            }
    }

    test(name = "Deserializing light entity response succeeds with optional attributes missing") {
        val encoded = """
            |{
            |    "type": "light",
            |    "identifier": "light.living-room",
            |    "state": {
            |        "onOff": true
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .with(EntityResponse::identifier) { isEqualTo("light.living-room") }
            .with(EntityResponse::state) {
                this
                    .isNotNull()
                    .isA<LightEntity.State>()
                    .with(LightEntity.State::onOff) { isEqualTo(true) }
                    .with(LightEntity.State::brightness) { isNull() }
            }
    }

    test(name = "Deserializing light entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "light",
            |    "identifier": "light.living-room"
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .with(EntityResponse::identifier) { isEqualTo("light.living-room") }
            .with(EntityResponse::state) { isNull() }
    }

    test(name = "Deserializing light entity response fails with missing identifier") {
        expectThrows<IllegalArgumentException> {
            val encoded = """
                |{
                |    "type": "light",
                |}
            """.trimMargin()

            json.decodeFromString<EntityResponse>(string = encoded)
        }
    }

    test(name = "Deserializing light entity response fails with missing required state") {
        expectThrows<IllegalArgumentException> {
            val encoded = """
                |{
                |    "type": "light",
                |    "identifier": "light.living-room",
                |    "state": {
                |        "brightness": 0.5
                |    }
                |}
            """.trimMargin()

            json.decodeFromString<EntityResponse>(string = encoded)
        }
    }
}
