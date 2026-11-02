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
import network.marsys.smarthome.domain.capability.ThermostatMode
import network.marsys.smarthome.domain.capability.ThermostatMode.Mode

val ThermostatEntityResponseSnapshotTest by testSuite(
    name = "Thermostat entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Serializing thermostat entity response succeeds") {
        val response = ThermostatEntityResponse(
            identifier = "thermostat.living-room",
            naming = EntityResponse.Naming(
                key = "thermostat.living-room",
            ),
            state = ThermostatEntityResponse.State(
                onOff = true,
                mode = Mode.Auto.name,
                temperatures = ThermostatEntityResponse.Temperatures(
                    target = 25.0,
                    current = 20.5,
                    outdoor = 8.5,
                ),
                childLock = true,
                windowDetection = true,
                scheduledMode = false,
            ),
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "thermostat",
                    |    "identifier": "thermostat.living-room",
                    |    "naming": {
                    |        "key": "thermostat.living-room"
                    |    },
                    |    "state": {
                    |        "onOff": true,
                    |        "mode": "Auto",
                    |        "temperatures": {
                    |            "target": 25.0,
                    |            "current": 20.5,
                    |            "outdoor": 8.5
                    |        },
                    |        "child-lock": true,
                    |        "window-detection": true,
                    |        "scheduled-mode": false
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Serializing thermostat entity response succeeds with unknown state") {
        val response = ThermostatEntityResponse(
            identifier = "thermostat.living-room",
            naming = EntityResponse.Naming(
                key = "thermostat.living-room",
            ),
            state = null,
        )

        val encoded = json.encodeToString(serializer = EntityResponse.serializer(), value = response)

        expectThat(encoded)
            .isEqualTo(
                """
                    |{
                    |    "type": "thermostat",
                    |    "identifier": "thermostat.living-room",
                    |    "naming": {
                    |        "key": "thermostat.living-room"
                    |    }
                    |}
                """.trimMargin(),
            )
    }

    test(name = "Deserializing thermostat entity response succeeds") {
        val encoded = """
            |{
            |    "type": "thermostat",
            |    "identifier": "thermostat.living-room",
            |    "naming": {
            |        "key": "thermostat.living-room"
            |    },
            |    "state": {
            |        "onOff": true,
            |        "mode": "Auto",
            |        "temperatures": {
            |            "target": 25.0,
            |            "current": 20.5,
            |            "outdoor": 8.5
            |        },
            |        "child-lock": true,
            |        "window-detection": true,
            |        "scheduled-mode": false
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isA<ThermostatEntityResponse.State>()
    }

    test(name = "Deserializing thermostat entity response succeeds with unknown state") {
        val encoded = """
            |{
            |    "type": "thermostat",
            |    "identifier": "thermostat.living-room",
            |    "naming": {
            |        "key": "thermostat.living-room"
            |    }
            |}
        """.trimMargin()

        val decoded = json.decodeFromString<EntityResponse>(string = encoded)

        expectThat(decoded)
            .get(EntityResponse::state)
            .isNull()
    }
}
