package network.marsys.smarthome.api.mappers

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.all
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.api.models.zone.ZoneResponse
import network.marsys.smarthome.domain.TranslationKey
import network.marsys.smarthome.domain.zone.Zone
import network.marsys.smarthome.domain.zone.ZoneIdentifier
import network.marsys.smarthome.domain.zone.ZoneNaming

private val zones = listOf(
    Zone(
        identifier = ZoneIdentifier("zone.living-room"),
        naming = ZoneNaming(
            key = TranslationKey("living-room"),
        ),
    ),
    Zone(
        identifier = ZoneIdentifier("zone.bedroom"),
        naming = ZoneNaming(
            key = TranslationKey("bedroom"),
        ),
    ),
    Zone(
        identifier = ZoneIdentifier("zone.bathroom"),
        naming = ZoneNaming(
            key = TranslationKey("bathroom"),
        ),
    ),
)

val ZoneMapperTest by testSuite(
    name = "Zone mapper tests",
) {
    test(name = "Mapping zone collection to API model") {
        expectThat(ZoneCollectionApiMapper.invoke(source = zones))
            .with(Collection<*>::size) { isEqualTo(zones.size) }
            .all {
                isA<ZoneResponse>()
            }
    }

    test(name = "Mapping API models back to zone collection") {
        val mapped = ZoneCollectionApiMapper.invoke(source = zones)

        expectThat(ZoneCollectionDomainMapper.invoke(source = mapped))
            .with(Collection<*>::size) { isEqualTo(zones.size) }
            .isEqualTo(zones)
    }
}
