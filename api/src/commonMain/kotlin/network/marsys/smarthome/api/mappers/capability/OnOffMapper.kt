package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.OnOff

object OnOffApiMapper : Mapper<OnOff, Boolean> {
    override fun invoke(source: OnOff) = source.current
}

object OnOffDomainMapper : Mapper<Boolean, OnOff> {
    override fun invoke(source: Boolean) = OnOff(current = source)
}
