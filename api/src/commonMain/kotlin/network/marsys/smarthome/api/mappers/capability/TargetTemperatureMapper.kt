package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.TargetTemperature
import network.marsys.smarthome.domain.unit.celsius

object TargetTemperatureApiMapper : Mapper<TargetTemperature, Double> {
    override fun invoke(source: TargetTemperature) = source.current.value
}

object TargetTemperatureDomainMapper : Mapper<Double, TargetTemperature> {
    override fun invoke(source: Double) = TargetTemperature(current = source.celsius)
}
