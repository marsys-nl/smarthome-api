package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.Movement

object MovementApiMapper : Mapper<Movement, String> {
    override fun invoke(source: Movement) = source.current.name
}

object MovementDomainMapper : Mapper<String, Movement> {
    override fun invoke(source: String) = Movement(current = Movement.Direction.valueOf(source))
}
