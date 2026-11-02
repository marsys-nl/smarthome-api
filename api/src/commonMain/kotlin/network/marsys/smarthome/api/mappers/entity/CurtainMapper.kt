package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.models.entity.CurtainEntityResponse
import network.marsys.smarthome.domain.entity.Curtain
import network.marsys.smarthome.domain.entity.EntityIdentifier

object CurtainApiMapper : Mapper<Curtain, CurtainEntityResponse> {
    override fun invoke(source: Curtain) = CurtainEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is Curtain.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: Curtain.State.Known) = CurtainEntityResponse.State(
        control = CoverControlApiMapper.invoke(source = state.control),
    )
}

object CurtainDomainMapper : Mapper<CurtainEntityResponse, Curtain> {
    override fun invoke(source: CurtainEntityResponse) = Curtain(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is CurtainEntityResponse.State -> map(state = state)
            else -> Curtain.State.Unknown
        },
    )

    private fun map(state: CurtainEntityResponse.State) = Curtain.State.Known(
        control = CoverControlDomainMapper.invoke(source = state.control),
    )
}
