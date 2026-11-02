package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintApiMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.required
import network.marsys.smarthome.api.mappers.capability.OnOffApiMapper
import network.marsys.smarthome.api.mappers.capability.OnOffDomainMapper
import network.marsys.smarthome.api.models.entity.SmartLockEntityResponse
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.SmartLock

object SmartLockApiMapper : Mapper<SmartLock, SmartLockEntityResponse> {
    override fun invoke(source: SmartLock) = SmartLockEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is SmartLock.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: SmartLock.State.Known) = SmartLockEntityResponse.State(
        onOff = CapabilityConstraintApiMapper.invoke(source = state.onOff, mapper = OnOffApiMapper),
    )
}

object SmartLockDomainMapper : Mapper<SmartLockEntityResponse, SmartLock> {
    override fun invoke(source: SmartLockEntityResponse) = SmartLock(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is SmartLockEntityResponse.State -> map(state = state)
            else -> SmartLock.State.Unknown
        },
    )

    private fun map(state: SmartLockEntityResponse.State) = SmartLock.State.Known(
        onOff = required(source = state.onOff, mapper = OnOffDomainMapper),
    )
}
