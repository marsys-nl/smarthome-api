package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintApiMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.required
import network.marsys.smarthome.api.mappers.capability.OnOffApiMapper
import network.marsys.smarthome.api.mappers.capability.OnOffDomainMapper
import network.marsys.smarthome.api.models.entity.CameraEntityResponse
import network.marsys.smarthome.domain.entity.Camera
import network.marsys.smarthome.domain.entity.EntityIdentifier

object CameraApiMapper : Mapper<Camera, CameraEntityResponse> {
    override fun invoke(source: Camera) = CameraEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is Camera.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: Camera.State.Known) = CameraEntityResponse.State(
        onOff = CapabilityConstraintApiMapper.invoke(source = state.onOff, mapper = OnOffApiMapper),
    )
}

object CameraDomainMapper : Mapper<CameraEntityResponse, Camera> {
    override fun invoke(source: CameraEntityResponse) = Camera(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is CameraEntityResponse.State -> map(state = state)
            else -> Camera.State.Unknown
        },
    )

    private fun map(state: CameraEntityResponse.State) = Camera.State.Known(
        onOff = required(source = state.onOff, mapper = OnOffDomainMapper),
    )
}
