package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.ScheduledMode

object ScheduledModeApiMapper : Mapper<ScheduledMode, Boolean> {
    override fun invoke(source: ScheduledMode) = source.current
}

object ScheduledModeDomainMapper : Mapper<Boolean, ScheduledMode> {
    override fun invoke(source: Boolean) = ScheduledMode(current = source)
}
