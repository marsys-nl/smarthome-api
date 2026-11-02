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

val CurtainEntityResponseSnapshotTest by testSuite(
    name = "Curtain entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing curtain entity response succeeds") {
        val response = CurtainEntityResponse(
            identifier = "curtain.living-room",
            naming = EntityResponse.Naming(
                key = "curtain.kitchen",
            ),
            state = CurtainEntityResponse.State(
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
                    |    "type": "curtain",
                    |    "identifier": "curtain.living-room",
                    |    "naming": {
                    |        "key": "curtain.kitchen"
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

    test(name = "Serializing curtain entity response succeeds with unknown state") {
        val response = CurtainEntityResponse(
            identifier = "curtain.living-room",
            naming = EntityResponse.Naming(
                key = "curtain.living-room",
            ),
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "curtain",
                    |    "identifier": "curtain.living-room",
                    |    "naming": {
                    |        "key": "curtain.living-room"
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing curtain entity response succeeds") {
        val encoded = """
            |{
            |    "type": "curtain",
            |    "identifier": "curtain.living-room",
            |    "naming": {
            |        "key": "curtain.living-room"
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
            .isA<CurtainEntityResponse.State>()
            .get(CurtainEntityResponse.State::control)
            .isA<CoverControlResponse>()
            .with(CoverControlResponse::movement) { isEqualTo(Idle.name) }
            .with(CoverControlResponse::opened) { isEqualTo(true) }
            .with(CoverControlResponse::position) { isEqualTo(50.0) }
    }

    test(name = "Deserializing curtain entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "curtain",
            |    "identifier": "curtain.living-room",
            |    "naming": {
            |        "key": "curtain.living-room"
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isNull()
    }
}
