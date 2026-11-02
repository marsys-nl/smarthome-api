package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintApiMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.optional
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.required
import network.marsys.smarthome.api.mappers.capability.MovementApiMapper
import network.marsys.smarthome.api.mappers.capability.MovementDomainMapper
import network.marsys.smarthome.api.mappers.capability.OpenedApiMapper
import network.marsys.smarthome.api.mappers.capability.OpenedDomainMapper
import network.marsys.smarthome.api.mappers.capability.PositionApiMapper
import network.marsys.smarthome.api.mappers.capability.PositionDomainMapper
import network.marsys.smarthome.api.models.entity.CoverControlResponse
import network.marsys.smarthome.domain.entity.Cover

object CoverControlApiMapper : Mapper<Cover.Control, CoverControlResponse> {
    override fun invoke(source: Cover.Control) = when (source) {
        is Cover.Control.WithPosition ->
            CoverControlResponse(
                position = CapabilityConstraintApiMapper.invoke(source = source.position, mapper = PositionApiMapper),
                movement = CapabilityConstraintApiMapper.invoke(source = source.movement, mapper = MovementApiMapper),
            )

        is Cover.Control.WithoutPosition ->
            CoverControlResponse(
                opened = CapabilityConstraintApiMapper.invoke(source = source.opened, mapper = OpenedApiMapper),
                movement = CapabilityConstraintApiMapper.invoke(source = source.movement, mapper = MovementApiMapper),
            )
    }
}

object CoverControlDomainMapper : Mapper<CoverControlResponse, Cover.Control> {
    override fun invoke(source: CoverControlResponse): Cover.Control {
        val movement = optional(source = source.movement, mapper = MovementDomainMapper)

        return when {
            source.position != null && source.opened != null ->
                error("Invalid cover control response: both position and opened cannot be present")

            source.position != null ->
                Cover.Control.WithPosition(
                    position = required(source = source.position, mapper = PositionDomainMapper),
                    movement = movement,
                )

            source.opened != null ->
                Cover.Control.WithoutPosition(
                    opened = required(source = source.opened, mapper = OpenedDomainMapper),
                    movement = movement,
                )

            else -> error("Invalid cover control response: either position or opened must be present")
        }
    }
}
