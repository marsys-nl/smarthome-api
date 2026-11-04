package network.marsys.smarthome.api.mappers

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.TranslationKey
import network.marsys.smarthome.domain.entity.EntityNaming

val EntityNamingMapperTest by testSuite(
    name = "Entity naming mapper tests",
) {
    testSuite(
        name = "Map entity naming to API model",
    ) {
        test(name = "Mapping succeeds when both key and qualifier are present") {
            val source = EntityNaming(
                key = TranslationKey("light.living-room"),
                qualifier = TranslationKey("ceiling"),
            )

            expectThat(EntityNamingApiMapper.invoke(source))
                .with(EntityResponse.Naming::key) { isEqualTo("light.living-room") }
                .with(EntityResponse.Naming::qualifier) { isEqualTo("ceiling") }
        }

        test(name = "Mapping succeeds when only key is present") {
            val source = EntityNaming(
                key = TranslationKey("light.living-room"),
            )

            expectThat(EntityNamingApiMapper.invoke(source))
                .with(EntityResponse.Naming::key) { isEqualTo("light.living-room") }
                .with(EntityResponse.Naming::qualifier) { isNull() }
        }
    }

    testSuite(
        name = "Map API model to entity naming",
    ) {
        test(name = "Mapping succeeds when both key and qualifier are present") {
            val source = EntityResponse.Naming(
                key = "light.living-room",
                qualifier = "ceiling",
            )

            expectThat(EntityNamingDomainMapper.invoke(source))
                .with(EntityNaming::key) { isEqualTo(TranslationKey("light.living-room")) }
                .with(EntityNaming::qualifier) { isEqualTo(TranslationKey("ceiling")) }
        }

        test(name = "Mapping succeeds when only key is present") {
            val source = EntityResponse.Naming(
                key = "light.living-room",
            )

            expectThat(EntityNamingDomainMapper.invoke(source))
                .with(EntityNaming::key) { isEqualTo(TranslationKey("light.living-room")) }
                .with(EntityNaming::qualifier) { isNull() }
        }
    }
}
