package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintApiMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.optional
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.required
import network.marsys.smarthome.api.mappers.capability.ChildLockApiMapper
import network.marsys.smarthome.api.mappers.capability.ChildLockDomainMapper
import network.marsys.smarthome.api.mappers.capability.MeasureTemperatureApiMapper
import network.marsys.smarthome.api.mappers.capability.MeasureTemperatureDomainMapper
import network.marsys.smarthome.api.mappers.capability.OnOffApiMapper
import network.marsys.smarthome.api.mappers.capability.OnOffDomainMapper
import network.marsys.smarthome.api.mappers.capability.ScheduledModeApiMapper
import network.marsys.smarthome.api.mappers.capability.ScheduledModeDomainMapper
import network.marsys.smarthome.api.mappers.capability.TargetTemperatureApiMapper
import network.marsys.smarthome.api.mappers.capability.TargetTemperatureDomainMapper
import network.marsys.smarthome.api.mappers.capability.ThermostatModeApiMapper
import network.marsys.smarthome.api.mappers.capability.ThermostatModeDomainMapper
import network.marsys.smarthome.api.mappers.capability.WindowDetectionApiMapper
import network.marsys.smarthome.api.mappers.capability.WindowDetectionDomainMapper
import network.marsys.smarthome.api.models.entity.ThermostatEntityResponse
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Thermostat

object ThermostatApiMapper : Mapper<Thermostat, ThermostatEntityResponse> {
    override fun invoke(source: Thermostat) = ThermostatEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is Thermostat.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: Thermostat.State.Known) = ThermostatEntityResponse.State(
        onOff = CapabilityConstraintApiMapper.invoke(source = state.onOff, mapper = OnOffApiMapper),
        mode = CapabilityConstraintApiMapper.invoke(source = state.mode, mapper = ThermostatModeApiMapper),
        temperatures = ThermostatEntityResponse.Temperatures(
            target = CapabilityConstraintApiMapper
                .invoke(source = state.temperatures.target, mapper = TargetTemperatureApiMapper),
            current = CapabilityConstraintApiMapper
                .invoke(source = state.temperatures.current, mapper = MeasureTemperatureApiMapper),
            outdoor = CapabilityConstraintApiMapper
                .invoke(source = state.temperatures.outdoor, mapper = MeasureTemperatureApiMapper),
        ),
        childLock = CapabilityConstraintApiMapper
            .invoke(source = state.childLock, mapper = ChildLockApiMapper),
        windowDetection = CapabilityConstraintApiMapper
            .invoke(source = state.windowDetection, mapper = WindowDetectionApiMapper),
        scheduledMode = CapabilityConstraintApiMapper
            .invoke(source = state.scheduledMode, mapper = ScheduledModeApiMapper),
    )
}

object ThermostatDomainMapper : Mapper<ThermostatEntityResponse, Thermostat> {
    override fun invoke(source: ThermostatEntityResponse) = Thermostat(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is ThermostatEntityResponse.State -> map(state = state)
            else -> Thermostat.State.Unknown
        },
    )

    private fun map(state: ThermostatEntityResponse.State) = Thermostat.State.Known(
        onOff = required(source = state.onOff, mapper = OnOffDomainMapper),
        mode = required(source = state.mode, mapper = ThermostatModeDomainMapper),
        temperatures = Thermostat.Temperatures(
            target = required(source = state.temperatures.target, mapper = TargetTemperatureDomainMapper),
            current = required(source = state.temperatures.current, mapper = MeasureTemperatureDomainMapper),
            outdoor = optional(source = state.temperatures.outdoor, mapper = MeasureTemperatureDomainMapper),
        ),
        childLock = optional(source = state.childLock, mapper = ChildLockDomainMapper),
        windowDetection = optional(source = state.windowDetection, mapper = WindowDetectionDomainMapper),
        scheduledMode = optional(source = state.scheduledMode, mapper = ScheduledModeDomainMapper),
    )
}
