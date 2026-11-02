package network.marsys.smarthome.api.mappers

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.EntityNaming
import network.marsys.smarthome.domain.i18n.TranslationKey

val EntityIdentifierMapperTest by testSuite(
    name = "Entity identifier mapper tests",
) {
    testSuite(
        name = "Map entity identifier to API model",
    ) {
        test(name = "Mapping succeeds when identifier is present") {
            val source = EntityIdentifier("light.living-room")

            expectThat(EntityIdentifierApiMapper.invoke(source))
                .isEqualTo("light.living-room")
        }
    }

    testSuite(
        name = "Map API model to entity naming",
    ) {
        test(name = "Mapping succeeds when identifier is present") {
            val source = "light.living-room"

            expectThat(EntityIdentifierDomainMapper.invoke(source))
                .isA<EntityIdentifier>()
                .get(EntityIdentifier::value)
                .isEqualTo("light.living-room")
        }
    }
}
