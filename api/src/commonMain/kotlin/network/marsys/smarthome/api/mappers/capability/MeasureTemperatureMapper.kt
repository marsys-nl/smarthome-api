package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.MeasureTemperature
import network.marsys.smarthome.domain.unit.celsius

object MeasureTemperatureApiMapper : Mapper<MeasureTemperature, Double> {
    override fun invoke(source: MeasureTemperature) = source.current.value
}

object MeasureTemperatureDomainMapper : Mapper<Double, MeasureTemperature> {
    override fun invoke(source: Double) = MeasureTemperature(current = source.celsius)
}
