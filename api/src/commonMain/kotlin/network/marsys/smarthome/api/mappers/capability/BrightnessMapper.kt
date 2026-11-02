package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.unit.percent

object BrightnessApiMapper : Mapper<Brightness, Double> {
    override fun invoke(source: Brightness) = source.current.value
}

object BrightnessDomainMapper : Mapper<Double, Brightness> {
    override fun invoke(source: Double) = Brightness(current = source.percent)
}
