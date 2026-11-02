package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.Opened

object OpenedApiMapper : Mapper<Opened, Boolean> {
    override fun invoke(source: Opened) = source.current
}

object OpenedDomainMapper : Mapper<Boolean, Opened> {
    override fun invoke(source: Boolean) = Opened(current = source)
}
