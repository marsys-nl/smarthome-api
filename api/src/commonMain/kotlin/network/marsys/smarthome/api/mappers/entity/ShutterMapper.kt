package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.models.entity.ShutterEntityResponse
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Shutter

object ShutterApiMapper : Mapper<Shutter, ShutterEntityResponse> {
    override fun invoke(source: Shutter) = ShutterEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is Shutter.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: Shutter.State.Known) = ShutterEntityResponse.State(
        control = CoverControlApiMapper.invoke(source = state.control),
    )
}

object ShutterDomainMapper : Mapper<ShutterEntityResponse, Shutter> {
    override fun invoke(source: ShutterEntityResponse) = Shutter(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is ShutterEntityResponse.State -> map(state = state)
            else -> Shutter.State.Unknown
        },
    )

    private fun map(state: ShutterEntityResponse.State) = Shutter.State.Known(
        control = CoverControlDomainMapper.invoke(source = state.control),
    )
}
