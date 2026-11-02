package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.ThermostatMode

object ThermostatModeApiMapper : Mapper<ThermostatMode, String> {
    override fun invoke(source: ThermostatMode) = source.current.name
}

object ThermostatModeDomainMapper : Mapper<String, ThermostatMode> {
    override fun invoke(source: String) = ThermostatMode(current = ThermostatMode.Mode.valueOf(source))
}
