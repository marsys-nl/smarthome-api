package network.marsys.smarthome.api.mappers

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.zone.ZoneResponse
import network.marsys.smarthome.domain.TranslationKey
import network.marsys.smarthome.domain.zone.ZoneNaming

val ZoneNamingMapperTest by testSuite(
    name = "Zone naming mapper tests",
) {
    testSuite(
        name = "Map zone naming to API model",
    ) {
        test(name = "Mapping succeeds when both key and qualifier are present") {
            val source = ZoneNaming(
                key = TranslationKey("living-room"),
                qualifier = TranslationKey("ceiling"),
            )

            expectThat(ZoneNamingApiMapper.invoke(source))
                .with(ZoneResponse.Naming::key) { isEqualTo("living-room") }
                .with(ZoneResponse.Naming::qualifier) { isEqualTo("ceiling") }
        }

        test(name = "Mapping succeeds when only key is present") {
            val source = ZoneNaming(
                key = TranslationKey("living-room"),
            )

            expectThat(ZoneNamingApiMapper.invoke(source))
                .with(ZoneResponse.Naming::key) { isEqualTo("living-room") }
                .with(ZoneResponse.Naming::qualifier) { isNull() }
        }
    }

    testSuite(
        name = "Map API model to zone naming",
    ) {
        test(name = "Mapping succeeds when both key and qualifier are present") {
            val source = ZoneResponse.Naming(
                key = "living-room",
                qualifier = "ceiling",
            )

            expectThat(ZoneNamingDomainMapper.invoke(source))
                .with(ZoneNaming::key) { isEqualTo(TranslationKey("living-room")) }
                .with(ZoneNaming::qualifier) { isEqualTo(TranslationKey("ceiling")) }
        }

        test(name = "Mapping succeeds when only key is present") {
            val source = ZoneResponse.Naming(
                key = "living-room",
            )

            expectThat(ZoneNamingDomainMapper.invoke(source))
                .with(ZoneNaming::key) { isEqualTo(TranslationKey("living-room")) }
                .with(ZoneNaming::qualifier) { isNull() }
        }
    }
}
