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

val BlindEntityResponseSnapshotTest by testSuite(
    name = "Blind entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing blind entity response succeeds") {
        val response = BlindEntityResponse(
            identifier = "blind.living-room",
            naming = EntityResponse.Naming(
                key = "blind.kitchen",
            ),
            state = BlindEntityResponse.State(
                control = CoverControlResponse(
                    movement = Idle.name,
                    opened = true,
                    position = 50.0,
                )
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "blind",
                    |    "identifier": "blind.living-room",
                    |    "naming": {
                    |        "key": "blind.kitchen"
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

    test(name = "Serializing blind entity response succeeds with unknown state") {
        val response = BlindEntityResponse(
            identifier = "blind.living-room",
            naming = EntityResponse.Naming(
                key = "blind.living-room",
            ),
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "blind",
                    |    "identifier": "blind.living-room",
                    |    "naming": {
                    |        "key": "blind.living-room"
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing blind entity response succeeds") {
        val encoded = """
            |{
            |    "type": "blind",
            |    "identifier": "blind.living-room",
            |    "naming": {
            |        "key": "blind.living-room"
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
            .isA<BlindEntityResponse.State>()
            .get(BlindEntityResponse.State::control)
            .isA<CoverControlResponse>()
            .with(CoverControlResponse::movement) { isEqualTo(Idle.name) }
            .with(CoverControlResponse::opened) { isEqualTo(true) }
            .with(CoverControlResponse::position) { isEqualTo(50.0) }
    }

    test(name = "Deserializing blind entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "blind",
            |    "identifier": "blind.living-room",
            |    "naming": {
            |        "key": "blind.living-room"
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isNull()
    }
}
