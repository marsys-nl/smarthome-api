package network.marsys.smarthome.api.models.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.hasMessageStartingWith
import kotlinx.serialization.json.Json
import network.marsys.smarthome.api.apiModuleSerializersModule

val EntityResponseSnapshotTest by testSuite(
    name = "General entity response snapshot tests",
) {
    val json = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = apiModuleSerializersModule
    }

    test(name = "Deserializing entity response fails when no type is set") {
        expectThrows<IllegalArgumentException> {
            val encoded = """
                |{
                |    "identifier": "light.living-room"
                |}
            """.trimMargin()

            json.decodeFromString<EntityResponse>(string = encoded)
        }.hasMessageStartingWith(
            prefix = "Class discriminator was missing and no default serializers " +
                "were registered in the polymorphic scope of 'EntityResponse'.",
        )
    }
}
