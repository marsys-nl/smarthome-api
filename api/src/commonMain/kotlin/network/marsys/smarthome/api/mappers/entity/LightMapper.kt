package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.mappers.capability.BrightnessApiMapper
import network.marsys.smarthome.api.mappers.capability.BrightnessDomainMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintApiMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.optional
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.required
import network.marsys.smarthome.api.mappers.capability.OnOffApiMapper
import network.marsys.smarthome.api.mappers.capability.OnOffDomainMapper
import network.marsys.smarthome.api.models.entity.LightEntityResponse
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Light

object LightApiMapper : Mapper<Light, LightEntityResponse> {
    override fun invoke(source: Light) = LightEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is Light.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: Light.State.Known) = LightEntityResponse.State(
        onOff = CapabilityConstraintApiMapper.invoke(source = state.onOff, mapper = OnOffApiMapper),
        brightness = CapabilityConstraintApiMapper.invoke(source = state.brightness, mapper = BrightnessApiMapper),
    )
}

object LightDomainMapper : Mapper<LightEntityResponse, Light> {
    override fun invoke(source: LightEntityResponse) = Light(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is LightEntityResponse.State -> map(state = state)
            else -> Light.State.Unknown
        },
    )

    private fun map(state: LightEntityResponse.State) = Light.State.Known(
        onOff = required(source = state.onOff, mapper = OnOffDomainMapper),
        brightness = optional(source = state.brightness, mapper = BrightnessDomainMapper),
    )
}
