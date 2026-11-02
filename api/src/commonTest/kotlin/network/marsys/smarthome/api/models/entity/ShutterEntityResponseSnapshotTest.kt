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
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.Movement.Direction.Idle

val ShutterEntityResponseSnapshotTest by testSuite(
    name = "Shutter entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing shutter entity response succeeds") {
        val response = ShutterEntityResponse(
            identifier = "shutter.living-room",
            naming = EntityResponse.Naming(
                key = "shutter.kitchen",
            ),
            state = ShutterEntityResponse.State(
                control = CoverControlResponse(
                    movement = Idle.name,
                    opened = true,
                    position = 50.0,
                ),
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "shutter",
                    |    "identifier": "shutter.living-room",
                    |    "naming": {
                    |        "key": "shutter.kitchen"
                    |    },
                    |    "state": {
                    |        "control": {
                    |            "movement": "Idle",
                    |            "opened": true,
                    |            "position": 50.0
                    |        }
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing shutter entity response succeeds with unknown state") {
        val response = ShutterEntityResponse(
            identifier = "shutter.living-room",
            naming = EntityResponse.Naming(
                key = "shutter.living-room",
            ),
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "shutter",
                    |    "identifier": "shutter.living-room",
                    |    "naming": {
                    |        "key": "shutter.living-room"
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing shutter entity response succeeds") {
        val encoded = """
            |{
            |    "type": "shutter",
            |    "identifier": "shutter.living-room",
            |    "naming": {
            |        "key": "shutter.living-room"
            |    },
            |    "state": {
            |        "control": {
            |            "movement": "Idle",
            |            "opened": true,
            |            "position": 50.0
            |        }
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isA<ShutterEntityResponse.State>()
            .get(ShutterEntityResponse.State::control)
            .isA<CoverControlResponse>()
            .with(CoverControlResponse::movement) { isEqualTo(Idle.name) }
            .with(CoverControlResponse::opened) { isEqualTo(true) }
            .with(CoverControlResponse::position) { isEqualTo(50.0) }
    }

    test(name = "Deserializing shutter entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "shutter",
            |    "identifier": "shutter.living-room",
            |    "naming": {
            |        "key": "shutter.living-room"
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isNull()
    }
}
