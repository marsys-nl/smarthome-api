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

val FanEntityResponseSnapshotTest by testSuite(
    name = "Fan entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing fan entity response succeeds") {
        val response = FanEntityResponse(
            identifier = "fan.bedroom",
            naming = EntityResponse.Naming(
                key = "fan.bedroom",
            ),
            state = FanEntityResponse.State(
                onOff = true,
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "fan",
                    |    "identifier": "fan.bedroom",
                    |    "naming": {
                    |        "key": "fan.bedroom"
                    |    },
                    |    "state": {
                    |        "onOff": true
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing fan entity response succeeds with unknown state") {
        val response = FanEntityResponse(
            identifier = "fan.bedroom",
            naming = EntityResponse.Naming(
                key = "fan.bedroom",
            ),
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "fan",
                    |    "identifier": "fan.bedroom",
                    |    "naming": {
                    |        "key": "fan.bedroom"
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing fan entity response succeeds") {
        val encoded = """
            |{
            |    "type": "fan",
            |    "identifier": "fan.bedroom",
            |    "naming": {
            |        "key": "fan.bedroom"
            |    },
            |    "state": {
            |        "onOff": true
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isA<FanEntityResponse.State>()
    }

    test(name = "Deserializing fan entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "fan",
            |    "identifier": "fan.bedroom",
            |    "naming": {
            |        "key": "fan.bedroom"
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isNull()
    }
}
