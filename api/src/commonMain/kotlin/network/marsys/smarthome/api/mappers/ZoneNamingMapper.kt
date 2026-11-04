package network.marsys.smarthome.api.mappers

import network.marsys.smarthome.api.models.zone.ZoneResponse
import network.marsys.smarthome.domain.TranslationKey
import network.marsys.smarthome.domain.zone.ZoneNaming

object ZoneNamingApiMapper : Mapper<ZoneNaming, ZoneResponse.Naming> {
    override fun invoke(source: ZoneNaming): ZoneResponse.Naming =
        ZoneResponse.Naming(
            key = source.key.value,
            qualifier = source.qualifier?.value,
        )
}

object ZoneNamingDomainMapper : Mapper<ZoneResponse.Naming, ZoneNaming> {
    override fun invoke(source: ZoneResponse.Naming): ZoneNaming =
        ZoneNaming(
            key = TranslationKey(source.key),
            qualifier = source.qualifier?.let { TranslationKey(it) },
        )
}
