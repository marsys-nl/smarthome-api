package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintApiMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.required
import network.marsys.smarthome.api.mappers.capability.OnOffApiMapper
import network.marsys.smarthome.api.mappers.capability.OnOffDomainMapper
import network.marsys.smarthome.api.models.entity.SpeakerEntityResponse
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Speaker

object SpeakerApiMapper : Mapper<Speaker, SpeakerEntityResponse> {
    override fun invoke(source: Speaker) = SpeakerEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is Speaker.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: Speaker.State.Known) = SpeakerEntityResponse.State(
        onOff = CapabilityConstraintApiMapper.invoke(source = state.onOff, mapper = OnOffApiMapper),
    )
}

object SpeakerDomainMapper : Mapper<SpeakerEntityResponse, Speaker> {
    override fun invoke(source: SpeakerEntityResponse) = Speaker(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is SpeakerEntityResponse.State -> map(state = state)
            else -> Speaker.State.Unknown
        },
    )

    private fun map(state: SpeakerEntityResponse.State) = Speaker.State.Known(
        onOff = required(source = state.onOff, mapper = OnOffDomainMapper),
    )
}
