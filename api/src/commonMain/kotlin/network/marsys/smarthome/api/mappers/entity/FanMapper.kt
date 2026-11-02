package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintApiMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.required
import network.marsys.smarthome.api.mappers.capability.OnOffApiMapper
import network.marsys.smarthome.api.mappers.capability.OnOffDomainMapper
import network.marsys.smarthome.api.models.entity.FanEntityResponse
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Fan

object FanApiMapper : Mapper<Fan, FanEntityResponse> {
    override fun invoke(source: Fan) = FanEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is Fan.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: Fan.State.Known) = FanEntityResponse.State(
        onOff = CapabilityConstraintApiMapper.invoke(source = state.onOff, mapper = OnOffApiMapper),
    )
}

object FanDomainMapper : Mapper<FanEntityResponse, Fan> {
    override fun invoke(source: FanEntityResponse) = Fan(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is FanEntityResponse.State -> map(state = state)
            else -> Fan.State.Unknown
        },
    )

    private fun map(state: FanEntityResponse.State) = Fan.State.Known(
        onOff = required(source = state.onOff, mapper = OnOffDomainMapper),
    )
}
