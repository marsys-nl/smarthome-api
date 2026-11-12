package network.marsys.smarthome.api.mappers.platform

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.hasMessage
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.api.models.platform.CapabilityMessage
import network.marsys.smarthome.api.models.platform.ContextMessage
import network.marsys.smarthome.domain.capability.Brightness
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
import network.marsys.smarthome.domain.entity.formattedName
import network.marsys.smarthome.domain.unit.Quantity
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.gigabytes
import network.marsys.smarthome.domain.unit.percent

val CapabilityMapperTest by testSuite(
    name = "Capability mapper tests",
) {
    val examples = mapOf(
        "brightness" to Brightness(current = 50.percent),
        "childLock" to ChildLock(current = true),
        "measureDataSize" to MeasureDataSize(current = 10.gigabytes),
        "measureLoad" to MeasureLoad(current = 10.percent),
        "measureTemperature" to MeasureTemperature(current = 21.celsius),
        "movement" to Movement(current = Movement.Direction.Idle),
        "onOff" to OnOff(current = true),
        "opened" to Opened(current = true),
        "position" to Position(current = 75.percent),
        "scheduledMode" to ScheduledMode(current = false),
        "targetTemperature" to TargetTemperature(current = 21.celsius),
        "thermostatMode" to ThermostatMode(current = ThermostatMode.Mode.Heat),
        "thermostatStatus" to ThermostatStatus(current = ThermostatStatus.Status.Off),
        "windowDetection" to WindowDetection(current = false),
    )

    testSuite(
        name = "Mapping a capability results in expected identifier",
    ) {
        examples.forEach { (identifier, capability) ->
            test(name = "Mapping a '${capability::class.formattedName}' will result in type '$identifier'") {
                expectThat(CapabilityApiMapper.invoke(source = capability))
                    .get(CapabilityMessage::type)
                    .isEqualTo(identifier)
            }
        }
    }

    testSuite(
        name = "Mapping a capability will result in a boolean value",
    ) {
        examples
            .filter { it.value.current is Boolean }
            .forEach { (_, capability) ->
                test(name = "Mapping a '${capability::class.formattedName}' will result in a boolean value") {
                    expectThat(CapabilityApiMapper.invoke(source = capability))
                        .isA<CapabilityMessage.BooleanValue>()
                }
            }
    }

    testSuite(
        name = "Mapping a capability will result in a double value",
    ) {
        examples
            .filter { it.value.current is Quantity<*> }
            .forEach { (_, capability) ->
                test(name = "Mapping a '${capability::class.formattedName}' will result in a double value") {
                    expectThat(CapabilityApiMapper.invoke(source = capability))
                        .isA<CapabilityMessage.DoubleValue>()
                }
            }
    }

    testSuite(
        name = "Mapping a capability will result in a enum value",
    ) {
        examples
            .filter { it.value.current is Enum<*> }
            .forEach { (_, capability) ->
                test(name = "Mapping a '${capability::class.formattedName}' will result in a enum value") {
                    expectThat(CapabilityApiMapper.invoke(source = capability))
                        .isA<CapabilityMessage.EnumValue>()
                }
            }
    }

    testSuite(
        name = "Mapping a boolean capability message results in expected capability",
    ) {
        examples
            .filter { it.value.current is Boolean }
            .forEach { (identifier, capability) ->
                test(name = "Mapping a boolean '$identifier' capability message will result in an expected capability") {
                    val message = CapabilityMessage.BooleanValue(
                        type = identifier,
                        context = ContextMessage(elements = emptyList()),
                        current = capability.current as Boolean,
                    )

                    expectThat(CapabilityDomainMapper.invoke(source = message))
                        .isEqualTo(capability)
                }
            }
    }

    testSuite(
        name = "Mapping a double capability message results in expected capability",
    ) {
        examples
            .filter { it.value.current is Quantity<*> }
            .forEach { (identifier, capability) ->
                test(name = "Mapping a double '$identifier' capability message will result in an expected capability") {
                    val message = CapabilityMessage.DoubleValue(
                        type = identifier,
                        context = ContextMessage(elements = emptyList()),
                        current = (capability.current as Quantity<*>).value,
                    )

                    expectThat(CapabilityDomainMapper.invoke(source = message))
                        .isEqualTo(capability)
                }
            }
    }

    testSuite(
        name = "Mapping a enum capability message results in expected capability",
    ) {
        examples
            .filter { it.value.current is Enum<*> }
            .forEach { (identifier, capability) ->
                test(name = "Mapping a enum '$identifier' capability message will result in an expected capability") {
                    val message = CapabilityMessage.EnumValue(
                        type = identifier,
                        context = ContextMessage(elements = emptyList()),
                        current = (capability.current as Enum<*>).name,
                    )

                    expectThat(CapabilityDomainMapper.invoke(source = message))
                        .isEqualTo(capability)
                }
            }
    }

    testSuite(
        name = "Mapping a capability message fails when unknown capability type is provided",
    ) {
        test(name = "Mapping a boolean capability message with unknown type will result in an exception") {
            val message = CapabilityMessage.BooleanValue(
                type = "unknown",
                context = ContextMessage(elements = emptyList()),
                current = true,
            )

            expectThrows<IllegalArgumentException> { CapabilityDomainMapper.invoke(source = message) }
                .hasMessage("Unknown capability type: 'unknown'")
        }

        test(name = "Mapping a double capability message with unknown type will result in an exception") {
            val message = CapabilityMessage.DoubleValue(
                type = "unknown",
                context = ContextMessage(elements = emptyList()),
                current = 50.0,
            )

            expectThrows<IllegalArgumentException> { CapabilityDomainMapper.invoke(source = message) }
                .hasMessage("Unknown capability type: 'unknown'")
        }

        test(name = "Mapping a enum capability message with unknown type will result in an exception") {
            val message = CapabilityMessage.EnumValue(
                type = "unknown",
                context = ContextMessage(elements = emptyList()),
                current = "some value",
            )

            expectThrows<IllegalArgumentException> { CapabilityDomainMapper.invoke(source = message) }
                .hasMessage("Unknown capability type: 'unknown'")
        }
    }
}
