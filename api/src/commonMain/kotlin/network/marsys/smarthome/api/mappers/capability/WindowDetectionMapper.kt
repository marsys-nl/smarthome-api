package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.WindowDetection

object WindowDetectionApiMapper : Mapper<WindowDetection, Boolean> {
    override fun invoke(source: WindowDetection) = source.current
}

object WindowDetectionDomainMapper : Mapper<Boolean, WindowDetection> {
    override fun invoke(source: Boolean) = WindowDetection(current = source)
}
