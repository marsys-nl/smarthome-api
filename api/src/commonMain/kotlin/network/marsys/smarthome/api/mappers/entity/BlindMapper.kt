package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.models.entity.BlindEntityResponse
import network.marsys.smarthome.domain.entity.Blind
import network.marsys.smarthome.domain.entity.EntityIdentifier

object BlindApiMapper : Mapper<Blind, BlindEntityResponse> {
    override fun invoke(source: Blind) = BlindEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is Blind.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: Blind.State.Known) = BlindEntityResponse.State(
        control = CoverControlApiMapper.invoke(source = state.control),
    )
}

object BlindDomainMapper : Mapper<BlindEntityResponse, Blind> {
    override fun invoke(source: BlindEntityResponse) = Blind(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is BlindEntityResponse.State -> map(state = state)
            else -> Blind.State.Unknown
        },
    )

    private fun map(state: BlindEntityResponse.State) = Blind.State.Known(
        control = CoverControlDomainMapper.invoke(source = state.control),
    )
}
