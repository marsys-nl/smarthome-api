package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.ThermostatMode
import network.marsys.smarthome.domain.capability.ThermostatMode.Mode.Auto

val ThermostatModeMapperTest by testSuite(
    name = "Thermostat mode capability mapper tests",
) {
    testSuite(
        name = "Map thermostat mode to API model",
    ) {
        test(name = "Mapping succeeds when thermostat mode is present") {
            val source = ThermostatMode(current = Auto)

            expectThat(ThermostatModeApiMapper.invoke(source))
                .isEqualTo(Auto.name)
        }
    }

    testSuite(
        name = "Map API model to thermostat mode",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = Auto.name

            expectThat(ThermostatModeDomainMapper.invoke(source))
                .isA<ThermostatMode>()
                .get(ThermostatMode::current)
                .isEqualTo(Auto)
        }
    }
}
