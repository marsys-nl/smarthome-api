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

val SpeakerEntityResponseSnapshotTest by testSuite(
    name = "Speaker entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing speaker entity response succeeds") {
        val response = SpeakerEntityResponse(
            identifier = "speaker.bedroom",
            naming = EntityResponse.Naming(
                key = "speaker.bedroom",
            ),
            state = SpeakerEntityResponse.State(
                onOff = true,
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "speaker",
                    |    "identifier": "speaker.bedroom",
                    |    "naming": {
                    |        "key": "speaker.bedroom"
                    |    },
                    |    "state": {
                    |        "onOff": true
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing speaker entity response succeeds with unknown state") {
        val response = SpeakerEntityResponse(
            identifier = "speaker.bedroom",
            naming = EntityResponse.Naming(
                key = "speaker.bedroom",
            ),
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "speaker",
                    |    "identifier": "speaker.bedroom",
                    |    "naming": {
                    |        "key": "speaker.bedroom"
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing speaker entity response succeeds") {
        val encoded = """
            |{
            |    "type": "speaker",
            |    "identifier": "speaker.bedroom",
            |    "naming": {
            |        "key": "speaker.bedroom"
            |    },
            |    "state": {
            |        "onOff": true
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isA<SpeakerEntityResponse.State>()
    }

    test(name = "Deserializing speaker entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "speaker",
            |    "identifier": "speaker.bedroom",
            |    "naming": {
            |        "key": "speaker.bedroom"
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isNull()
    }
}
