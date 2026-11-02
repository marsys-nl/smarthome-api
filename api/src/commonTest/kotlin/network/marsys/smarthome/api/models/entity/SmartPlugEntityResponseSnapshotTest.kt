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

val SmartPlugEntityResponseSnapshotTest by testSuite(
    name = "Smart plug entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing smart plug entity response succeeds") {
        val response = SmartPlugEntityResponse(
            identifier = "smart-plug.bedroom",
            naming = EntityResponse.Naming(
                key = "smart-plug.bedroom",
            ),
            state = SmartPlugEntityResponse.State(
                onOff = true,
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "smart-plug",
                    |    "identifier": "smart-plug.bedroom",
                    |    "naming": {
                    |        "key": "smart-plug.bedroom"
                    |    },
                    |    "state": {
                    |        "onOff": true
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing smart plug entity response succeeds with unknown state") {
        val response = SmartPlugEntityResponse(
            identifier = "smart-plug.bedroom",
            naming = EntityResponse.Naming(
                key = "smart-plug.bedroom",
            ),
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "smart-plug",
                    |    "identifier": "smart-plug.bedroom",
                    |    "naming": {
                    |        "key": "smart-plug.bedroom"
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing smart plug entity response succeeds") {
        val encoded = """
            |{
            |    "type": "smart-plug",
            |    "identifier": "smart-plug.bedroom",
            |    "naming": {
            |        "key": "smart-plug.bedroom"
            |    },
            |    "state": {
            |        "onOff": true
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isA<SmartPlugEntityResponse.State>()
    }

    test(name = "Deserializing smart plug entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "smart-plug",
            |    "identifier": "smart-plug.bedroom",
            |    "naming": {
            |        "key": "smart-plug.bedroom"
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isNull()
    }
}
