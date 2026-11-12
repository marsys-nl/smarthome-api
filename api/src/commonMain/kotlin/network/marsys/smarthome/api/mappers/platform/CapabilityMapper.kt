package network.marsys.smarthome.api.mappers.platform

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.models.platform.CapabilityMessage
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.capability.ChildLock
import network.marsys.smarthome.domain.capability.MeasureDataSize
import network.marsys.smarthome.domain.capability.MeasureLoad
import network.marsys.smarthome.domain.capability.MeasureTemperature
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.capability.Opened
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.capability.ScheduledMode
import network.marsys.smarthome.domain.capability.TargetTemperature
import network.marsys.smarthome.domain.capability.ThermostatMode
import network.marsys.smarthome.domain.capability.ThermostatStatus
import network.marsys.smarthome.domain.capability.WindowDetection
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.domain.unit.bytes
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.gigabytes
import network.marsys.smarthome.domain.unit.percent

internal object CapabilityApiMapper : Mapper<Capability<*, *>, CapabilityMessage> {
    private val Capability<*, *>.mapped
        get() = when (this) {
            is Brightness -> "brightness"
            is ChildLock -> "childLock"
            is MeasureDataSize -> "measureDataSize"
            is MeasureLoad -> "measureLoad"
            is MeasureTemperature -> "measureTemperature"
            is Movement -> "movement"
            is OnOff -> "onOff"
            is Opened -> "opened"
            is Position -> "position"
            is ScheduledMode -> "scheduledMode"
            is TargetTemperature -> "targetTemperature"
            is ThermostatMode -> "thermostatMode"
            is ThermostatStatus -> "thermostatStatus"
            is WindowDetection -> "windowDetection"
        }

    override fun invoke(source: Capability<*, *>): CapabilityMessage =
        when (val current = source.current) {
            is Boolean -> CapabilityMessage.BooleanValue(
                type = source.mapped,
                context = ContextApiMapper.invoke(source = source.context),
                current = current,
            )

            is Quantity<*> -> CapabilityMessage.DoubleValue(
                type = source.mapped,
                context = ContextApiMapper.invoke(source = source.context),
                current = current.value,
            )

            else -> CapabilityMessage.EnumValue(
                type = source.mapped,
                context = ContextApiMapper.invoke(source = source.context),
                current = current.toString(),
            )
        }
}

internal object CapabilityDomainMapper : Mapper<CapabilityMessage, Capability<*, *>> {
    override fun invoke(source: CapabilityMessage): Capability<*, *> =
        when (source) {
            is CapabilityMessage.BooleanValue -> source.map()
            is CapabilityMessage.DoubleValue -> source.map()
            is CapabilityMessage.EnumValue -> source.map()
        }

    private fun CapabilityMessage.BooleanValue.map(): Capability<*, *> =
        when (type) {
            "childLock" -> ChildLock(current = current)
            "onOff" -> OnOff(current = current)
            "opened" -> Opened(current = current)
            "scheduledMode" -> ScheduledMode(current = current)
            "windowDetection" -> WindowDetection(current = current)
            else -> throw IllegalArgumentException("Unknown capability type: '$type'")
        }

    private fun CapabilityMessage.DoubleValue.map(): Capability<*, *> =
        when (type) {
            "brightness" -> Brightness(current = current.percent)
            "measureDataSize" -> MeasureDataSize(current = current.bytes)
            "measureLoad" -> MeasureLoad(current = current.percent)
            "measureTemperature" -> MeasureTemperature(current = current.celsius)
            "position" -> Position(current = current.percent)
            "targetTemperature" -> TargetTemperature(current = current.celsius)
            else -> throw IllegalArgumentException("Unknown capability type: '$type'")
        }

    private fun CapabilityMessage.EnumValue.map(): Capability<*, *> =
        when (type) {
            "movement" -> Movement(current = Movement.Direction.valueOf(current))
            "thermostatMode" -> ThermostatMode(current = ThermostatMode.Mode.valueOf(current))
            else -> throw IllegalArgumentException("Unknown capability type: '$type'")
        }
}
