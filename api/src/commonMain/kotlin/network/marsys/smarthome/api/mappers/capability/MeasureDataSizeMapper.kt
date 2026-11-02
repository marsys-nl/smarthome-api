package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.MeasureDataSize
import network.marsys.smarthome.domain.unit.bytes

object MeasureDataSizeApiMapper : Mapper<MeasureDataSize, Double> {
    override fun invoke(source: MeasureDataSize) = source.current.value
}

object MeasureDataSizeDomainMapper : Mapper<Double, MeasureDataSize> {
    override fun invoke(source: Double) = MeasureDataSize(current = source.bytes)
}
