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

val SmartLockEntityResponseSnapshotTest by testSuite(
    name = "Smart lock entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing smart lock entity response succeeds") {
        val response = SmartLockEntityResponse(
            identifier = "smart-lock.bedroom",
            naming = EntityResponse.Naming(
                key = "smart-lock.bedroom",
            ),
            state = SmartLockEntityResponse.State(
                onOff = true,
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "smart-lock",
                    |    "identifier": "smart-lock.bedroom",
                    |    "naming": {
                    |        "key": "smart-lock.bedroom"
                    |    },
                    |    "state": {
                    |        "onOff": true
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing smart lock entity response succeeds with unknown state") {
        val response = SmartLockEntityResponse(
            identifier = "smart-lock.bedroom",
            naming = EntityResponse.Naming(
                key = "smart-lock.bedroom",
            ),
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "smart-lock",
                    |    "identifier": "smart-lock.bedroom",
                    |    "naming": {
                    |        "key": "smart-lock.bedroom"
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing smart lock entity response succeeds") {
        val encoded = """
            |{
            |    "type": "smart-lock",
            |    "identifier": "smart-lock.bedroom",
            |    "naming": {
            |        "key": "smart-lock.bedroom"
            |    },
            |    "state": {
            |        "onOff": true
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isA<SmartLockEntityResponse.State>()
    }

    test(name = "Deserializing smart lock entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "smart-lock",
            |    "identifier": "smart-lock.bedroom",
            |    "naming": {
            |        "key": "smart-lock.bedroom"
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isNull()
    }
}
