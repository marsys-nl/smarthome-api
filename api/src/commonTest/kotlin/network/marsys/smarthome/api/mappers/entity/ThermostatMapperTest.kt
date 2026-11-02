package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNotNull
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.api.models.entity.ThermostatEntityResponse
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.ChildLock
import network.marsys.smarthome.domain.capability.MeasureTemperature
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.capability.ScheduledMode
import network.marsys.smarthome.domain.capability.TargetTemperature
import network.marsys.smarthome.domain.capability.ThermostatMode
import network.marsys.smarthome.domain.capability.ThermostatMode.Mode
import network.marsys.smarthome.domain.capability.WindowDetection
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Thermostat
import network.marsys.smarthome.domain.entity.naming
import network.marsys.smarthome.domain.unit.celsius

val ThermostatMapperTest by testSuite(
    name = "Thermostat entity mapper tests",
) {
    testSuite(
        name = "Map thermostat entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = Thermostat(
                identifier = EntityIdentifier("thermostat.living-room"),
                naming = Thermostat.naming(),
                state = Thermostat.State.Known(
                    onOff = required(OnOff(current = true)),
                    mode = required(ThermostatMode(current = Mode.Auto)),
                    temperatures = Thermostat.Temperatures(
                        target = required(TargetTemperature(current = 22.0.celsius)),
                        current = required(MeasureTemperature(current = 21.0.celsius)),
                        outdoor = optional(null),
                    ),
                    childLock = optional(null),
                    windowDetection = optional(null),
                    scheduledMode = optional(null),
                ),
            )

            expectThat(ThermostatApiMapper.invoke(source))
                .get(ThermostatEntityResponse::state)
                .isA<ThermostatEntityResponse.State>()
        }

        test(name = "Mapping succeeds with known state - all capabilities present") {
            val source = Thermostat(
                identifier = EntityIdentifier("thermostat.living-room"),
                naming = Thermostat.naming(),
                state = Thermostat.State.Known(
                    onOff = required(OnOff(current = true)),
                    mode = required(ThermostatMode(current = Mode.Auto)),
                    temperatures = Thermostat.Temperatures(
                        target = required(TargetTemperature(current = 22.0.celsius)),
                        current = required(MeasureTemperature(current = 21.0.celsius)),
                        outdoor = optional(MeasureTemperature(current = 15.0.celsius)),
                    ),
                    childLock = optional(ChildLock(current = false)),
                    windowDetection = optional(WindowDetection(current = true)),
                    scheduledMode = optional(ScheduledMode(current = false)),
                ),
            )

            expectThat(ThermostatApiMapper.invoke(source))
                .get(ThermostatEntityResponse::state)
                .isA<ThermostatEntityResponse.State>()
                .with(ThermostatEntityResponse.State::temperatures) {
                    get(ThermostatEntityResponse.Temperatures::outdoor)
                        .isNotNull()
                }
                .with(ThermostatEntityResponse.State::childLock) { isNotNull() }
                .with(ThermostatEntityResponse.State::windowDetection) { isNotNull() }
                .with(ThermostatEntityResponse.State::scheduledMode) { isNotNull() }
        }

        test(name = "Mapping succeeds with known state - only required capabilities present") {
            val source = Thermostat(
                identifier = EntityIdentifier("thermostat.living-room"),
                naming = Thermostat.naming(),
                state = Thermostat.State.Known(
                    onOff = required(OnOff(current = true)),
                    mode = required(ThermostatMode(current = Mode.Auto)),
                    temperatures = Thermostat.Temperatures(
                        target = required(TargetTemperature(current = 22.0.celsius)),
                        current = required(MeasureTemperature(current = 21.0.celsius)),
                        outdoor = optional(null),
                    ),
                    childLock = optional(null),
                    windowDetection = optional(null),
                    scheduledMode = optional(null),
                ),
            )

            expectThat(ThermostatApiMapper.invoke(source))
                .get(ThermostatEntityResponse::state)
                .isA<ThermostatEntityResponse.State>()
                .with(ThermostatEntityResponse.State::temperatures) {
                    get(ThermostatEntityResponse.Temperatures::outdoor)
                        .isNull()
                }
                .with(ThermostatEntityResponse.State::childLock) { isNull() }
                .with(ThermostatEntityResponse.State::windowDetection) { isNull() }
                .with(ThermostatEntityResponse.State::scheduledMode) { isNull() }
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = Thermostat(
                identifier = EntityIdentifier("thermostat.living-room"),
                naming = Thermostat.naming(),
                state = Thermostat.State.Unknown,
            )

            expectThat(ThermostatApiMapper.invoke(source))
                .get(ThermostatEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to thermostat entity",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = ThermostatEntityResponse(
                identifier = "thermostat.living-room",
                naming = EntityResponse.Naming(key = "thermostat.living-room"),
                state = ThermostatEntityResponse.State(
                    onOff = true,
                    mode = Mode.Auto.name,
                    temperatures = ThermostatEntityResponse.Temperatures(
                        target = 22.0,
                        current = 21.0,
                        outdoor = null,
                    ),
                    childLock = null,
                    windowDetection = null,
                    scheduledMode = null,
                ),
            )

            expectThat(ThermostatDomainMapper.invoke(source))
                .get(Thermostat::state)
                .isA<Thermostat.State.Known>()
        }

        test(name = "Mapping succeeds with known state - all capabilities present") {
            val source = ThermostatEntityResponse(
                identifier = "thermostat.living-room",
                naming = EntityResponse.Naming(key = "thermostat.living-room"),
                state = ThermostatEntityResponse.State(
                    onOff = true,
                    mode = Mode.Auto.name,
                    temperatures = ThermostatEntityResponse.Temperatures(
                        target = 22.0,
                        current = 21.0,
                        outdoor = 15.0,
                    ),
                    childLock = true,
                    windowDetection = false,
                    scheduledMode = false,
                ),
            )

            expectThat(ThermostatDomainMapper.invoke(source))
                .get(Thermostat::state)
                .isA<Thermostat.State.Known>()
                .with(Thermostat.State.Known::temperatures) {
                    get(Thermostat.Temperatures::outdoor)
                        .isA<Capability.Available<MeasureTemperature>>()
                }
                .with(Thermostat.State.Known::childLock) { isA<Capability.Available<ChildLock>>() }
                .with(Thermostat.State.Known::windowDetection) { isA<Capability.Available<WindowDetection>>() }
                .with(Thermostat.State.Known::scheduledMode) { isA<Capability.Available<ScheduledMode>>() }
        }

        test(name = "Mapping succeeds with known state - only required capabilities present") {
            val source = ThermostatEntityResponse(
                identifier = "thermostat.living-room",
                naming = EntityResponse.Naming(key = "thermostat.living-room"),
                state = ThermostatEntityResponse.State(
                    onOff = true,
                    mode = Mode.Auto.name,
                    temperatures = ThermostatEntityResponse.Temperatures(
                        target = 22.0,
                        current = 21.0,
                        outdoor = null,
                    ),
                    childLock = null,
                    windowDetection = null,
                    scheduledMode = null,
                ),
            )

            expectThat(ThermostatDomainMapper.invoke(source))
                .get(Thermostat::state)
                .isA<Thermostat.State.Known>()
                .with(Thermostat.State.Known::temperatures) {
                    get(Thermostat.Temperatures::outdoor)
                        .isA<Capability.Unsupported>()
                }
                .with(Thermostat.State.Known::childLock) { isA<Capability.Unsupported>() }
                .with(Thermostat.State.Known::windowDetection) { isA<Capability.Unsupported>() }
                .with(Thermostat.State.Known::scheduledMode) { isA<Capability.Unsupported>() }
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = ThermostatEntityResponse(
                identifier = "thermostat.living-room",
                naming = EntityResponse.Naming(key = "thermostat.living-room"),
                state = null,
            )

            expectThat(ThermostatDomainMapper.invoke(source))
                .get(Thermostat::state)
                .isA<Thermostat.State.Unknown>()
        }
    }
}
