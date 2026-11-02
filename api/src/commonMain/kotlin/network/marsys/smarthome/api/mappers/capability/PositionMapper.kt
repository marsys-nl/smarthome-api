package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.unit.percent

object PositionApiMapper : Mapper<Position, Double> {
    override fun invoke(source: Position) = source.current.value
}

object PositionDomainMapper : Mapper<Double, Position> {
    override fun invoke(source: Double) = Position(current = source.percent)
}
