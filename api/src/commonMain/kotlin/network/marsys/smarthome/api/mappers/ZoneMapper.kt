package network.marsys.smarthome.api.mappers

import network.marsys.smarthome.api.models.zone.ZoneResponse
import network.marsys.smarthome.domain.zone.Zone
import network.marsys.smarthome.domain.zone.ZoneIdentifier

object ZoneApiMapper : Mapper<Zone, ZoneResponse> {
    override fun invoke(source: Zone) = ZoneResponse(
        identifier = source.identifier.value,
        naming = ZoneNamingApiMapper.invoke(source.naming),
    )
}

object ZoneCollectionApiMapper : Mapper<Collection<Zone>, Collection<ZoneResponse>> {
    override fun invoke(source: Collection<Zone>) = source.map(ZoneApiMapper::invoke)
}

object ZoneDomainMapper : Mapper<ZoneResponse, Zone> {
    override fun invoke(source: ZoneResponse) = Zone(
        identifier = ZoneIdentifier(value = source.identifier),
        naming = ZoneNamingDomainMapper.invoke(source.naming),
    )
}

object ZoneCollectionDomainMapper : Mapper<Collection<ZoneResponse>, Collection<Zone>> {
    override fun invoke(source: Collection<ZoneResponse>) = source.map(ZoneDomainMapper::invoke)
}
