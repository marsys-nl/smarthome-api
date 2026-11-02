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

val CameraEntityResponseSnapshotTest by testSuite(
    name = "Camera entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing camera entity response succeeds") {
        val response = CameraEntityResponse(
            identifier = "camera.front-door",
            naming = EntityResponse.Naming(
                key = "camera.front-door",
            ),
            state = CameraEntityResponse.State(
                onOff = true,
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "camera",
                    |    "identifier": "camera.front-door",
                    |    "naming": {
                    |        "key": "camera.front-door"
                    |    },
                    |    "state": {
                    |        "onOff": true
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing camera entity response succeeds with unknown state") {
        val response = CameraEntityResponse(
            identifier = "camera.front-door",
            naming = EntityResponse.Naming(
                key = "camera.front-door",
            ),
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "camera",
                    |    "identifier": "camera.front-door",
                    |    "naming": {
                    |        "key": "camera.front-door"
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing camera entity response succeeds") {
        val encoded = """
            |{
            |    "type": "camera",
            |    "identifier": "camera.front-door",
            |    "naming": {
            |        "key": "camera.front-door"
            |    },
            |    "state": {
            |        "onOff": true
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isA<CameraEntityResponse.State>()
    }

    test(name = "Deserializing camera entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "camera",
            |    "identifier": "camera.front-door",
            |    "naming": {
            |        "key": "camera.front-door"
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isNull()
    }
}
