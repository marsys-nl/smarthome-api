package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.MeasureLoad
import network.marsys.smarthome.domain.unit.percent

object MeasureLoadApiMapper : Mapper<MeasureLoad, Double> {
    override fun invoke(source: MeasureLoad) = source.current.value
}

object MeasureLoadDomainMapper : Mapper<Double, MeasureLoad> {
    override fun invoke(source: Double) = MeasureLoad(current = source.percent)
}
