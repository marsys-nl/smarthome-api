package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityIdentifierDomainMapper
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.EntityNaming
import network.marsys.smarthome.domain.i18n.TranslationKey
import network.marsys.smarthome.domain.unit.percent

val BrightnessMapperTest by testSuite(
    name = "Brightness capability mapper tests",
) {
    testSuite(
        name = "Map brightness to API model",
    ) {
        test(name = "Mapping succeeds when brightness is present") {
            val source = Brightness(current = 50.0.percent)

            expectThat(BrightnessApiMapper.invoke(source))
                .isEqualTo(50.0)
        }
    }

    testSuite(
        name = "Map API model to brightness",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = 50.0

            expectThat(BrightnessDomainMapper.invoke(source))
                .isA<Brightness>()
                .get(Brightness::current)
                .isEqualTo(50.0.percent)
        }
    }
}
