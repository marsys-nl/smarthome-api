package network.marsys.smarthome.api.models.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.hasMessageContaining
import dev.nmarsman.expect.assertions.hasMessageStartingWith
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isNull
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
                "were registered in the polymorphic scope of 'EntityResponse'",
        )
    }

    test(name = "Deserializing entity response fails when an unknown type is set") {
        expectThrows<IllegalArgumentException> {
            val encoded = """
                |{
                |    "type": "unknown",
                |    "identifier": "light.living-room"
                |}
            """.trimMargin()

            json.decodeFromString<EntityResponse>(string = encoded)
        }.hasMessageContaining(
            substring = "Serializer for subclass 'unknown' is not found in the polymorphic scope of 'EntityResponse'",
        )
    }

    testSuite(
        name = "Deserializing entity naming responses",
    ) {
        test(name = "Deserializing entity naming response succeeds when both key and qualifier are set") {
            val encoded = """
                |{
                |    "key": "light.living-room",
                |    "qualifier": "ceiling"
                |}
            """.trimMargin()

            val decoded = json.decodeFromString<EntityResponse.Naming>(string = encoded)

            expectThat(decoded)
                .with(EntityResponse.Naming::key) { isEqualTo("light.living-room") }
                .with(EntityResponse.Naming::qualifier) { isEqualTo("ceiling") }
        }

        test(name = "Deserializing entity naming response succeeds when only key set") {
            val encoded = """
                |{
                |    "key": "light.living-room"
                |}
            """.trimMargin()

            val decoded = json.decodeFromString<EntityResponse.Naming>(string = encoded)

            expectThat(decoded)
                .with(EntityResponse.Naming::key) { isEqualTo("light.living-room") }
                .with(EntityResponse.Naming::qualifier) { isNull() }
        }

        test(name = "Deserializing entity naming response fails when key isn't set") {
            expectThrows<IllegalArgumentException> {
                val encoded = """
                    |{
                    |    "qualifier": "ceiling"
                    |}
                """.trimMargin()

                json.decodeFromString<EntityResponse.Naming>(string = encoded)
            }.hasMessageStartingWith(
                prefix = "Field 'key' is required for type with serial name",
            )
        }
    }
}
