package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintApiMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.required
import network.marsys.smarthome.api.mappers.capability.OnOffApiMapper
import network.marsys.smarthome.api.mappers.capability.OnOffDomainMapper
import network.marsys.smarthome.api.models.entity.SmartPlugEntityResponse
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.SmartPlug

object SmartPlugApiMapper : Mapper<SmartPlug, SmartPlugEntityResponse> {
    override fun invoke(source: SmartPlug) = SmartPlugEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is SmartPlug.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: SmartPlug.State.Known) = SmartPlugEntityResponse.State(
        onOff = CapabilityConstraintApiMapper.invoke(source = state.onOff, mapper = OnOffApiMapper),
    )
}

object SmartPlugDomainMapper : Mapper<SmartPlugEntityResponse, SmartPlug> {
    override fun invoke(source: SmartPlugEntityResponse) = SmartPlug(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is SmartPlugEntityResponse.State -> map(state = state)
            else -> SmartPlug.State.Unknown
        },
    )

    private fun map(state: SmartPlugEntityResponse.State) = SmartPlug.State.Known(
        onOff = required(source = state.onOff, mapper = OnOffDomainMapper),
    )
}
