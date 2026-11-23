package network.marsys.smarthome.api.mappers.entity.action

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.api.models.entity.action.EntityActionRequest
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.capability.TargetTemperature
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Thermostat
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent

val EntityActionMapperTest by testSuite(
    name = "Entity action mapper tests",
) {
    testSuite(
        name = "Map entity action to API model",
    ) {
        test(name = "Mapping set brightness action to API model") {
            val action = Brightness.SetBrightness(
                entity = EntityIdentifier("light.kitchen"),
                brightness = 50.percent,
            )

            expectThat(EntityActionApiMapper.invoke(source = action))
                .isA<EntityActionRequest.SetBrightness>()
                .get(EntityActionRequest.SetBrightness::brightness)
                .isEqualTo(50.0)
        }

        test(name = "Mapping movement open action to API model") {
            val action = Movement.Move.Open(
                entity = EntityIdentifier("curtain.kitchen"),
            )

            expectThat(EntityActionApiMapper.invoke(source = action))
                .isA<EntityActionRequest.Open>()
        }

        test(name = "Mapping movement close action to API model") {
            val action = Movement.Move.Close(
                entity = EntityIdentifier("curtain.kitchen"),
            )

            expectThat(EntityActionApiMapper.invoke(source = action))
                .isA<EntityActionRequest.Close>()
        }

        test(name = "Mapping movement stop action to API model") {
            val action = Movement.Move.Stop(
                entity = EntityIdentifier("curtain.kitchen"),
            )

            expectThat(EntityActionApiMapper.invoke(source = action))
                .isA<EntityActionRequest.Stop>()
        }

        test(name = "Mapping on-off on action to API model") {
            val action = OnOff.Toggle.On(
                entity = EntityIdentifier("light.kitchen"),
            )

            expectThat(EntityActionApiMapper.invoke(source = action))
                .isA<EntityActionRequest.TurnOn>()
        }

        test(name = "Mapping on-off off action to API model") {
            val action = OnOff.Toggle.Off(
                entity = EntityIdentifier("light.kitchen"),
            )

            expectThat(EntityActionApiMapper.invoke(source = action))
                .isA<EntityActionRequest.TurnOff>()
        }

        test(name = "Mapping target temperature set temperature action to API model") {
            val action = TargetTemperature.SetTargetTemperature(
                entity = EntityIdentifier("thermostat.kitchen"),
                temperature = 22.0.celsius,
            )

            expectThat(EntityActionApiMapper.invoke(source = action))
                .isA<EntityActionRequest.SetTargetTemperature>()
                .get(EntityActionRequest.SetTargetTemperature::temperature)
                .isEqualTo(22.0)
        }
    }

    testSuite(
        name = "Map API model to entity action",
    ) {
        test(name = "Mapping set brightness action request to domain model") {
            val request = EntityActionRequest.SetBrightness(
                entity = "light.kitchen",
                brightness = 50.0,
            )

            expectThat(EntityActionDomainMapper.invoke(source = request))
                .isA<Brightness.SetBrightness>()
                .with(Brightness.SetBrightness::entity) {
                    isEqualTo(EntityIdentifier("light.kitchen"))
                }
                .with(Brightness.SetBrightness::brightness) {
                    isEqualTo(50.percent)
                }
        }

        test(name = "Mapping movement open action request to domain model") {
            val request = EntityActionRequest.Open(
                entity = "curtain.kitchen",
            )

            expectThat(EntityActionDomainMapper.invoke(source = request))
                .isA<Movement.Move.Open>()
                .get(Movement.Move.Open::entity)
                .isEqualTo(EntityIdentifier("curtain.kitchen"))
        }

        test(name = "Mapping movement close action request to domain model") {
            val request = EntityActionRequest.Close(
                entity = "curtain.kitchen",
            )

            expectThat(EntityActionDomainMapper.invoke(source = request))
                .isA<Movement.Move.Close>()
                .get(Movement.Move.Close::entity)
                .isEqualTo(EntityIdentifier("curtain.kitchen"))
        }

        test(name = "Mapping movement stop action request to domain model") {
            val request = EntityActionRequest.Stop(
                entity = "curtain.kitchen",
            )

            expectThat(EntityActionDomainMapper.invoke(source = request))
                .isA<Movement.Move.Stop>()
                .get(Movement.Move.Stop::entity)
                .isEqualTo(EntityIdentifier("curtain.kitchen"))
        }

        test(name = "Mapping on-off on action request to domain model") {
            val request = EntityActionRequest.TurnOn(
                entity = "light.kitchen",
            )

            expectThat(EntityActionDomainMapper.invoke(source = request))
                .isA<OnOff.Toggle.On>()
                .get(OnOff.Toggle.On::entity)
                .isEqualTo(EntityIdentifier("light.kitchen"))
        }

        test(name = "Mapping on-off off action request to domain model") {
            val request = EntityActionRequest.TurnOff(
                entity = "light.kitchen",
            )

            expectThat(EntityActionDomainMapper.invoke(source = request))
                .isA<OnOff.Toggle.Off>()
                .get(OnOff.Toggle.Off::entity)
                .isEqualTo(EntityIdentifier("light.kitchen"))
        }

        test(name = "Mapping set target temperature action request to domain model") {
            val request = EntityActionRequest.SetTargetTemperature(
                entity = "thermostat.kitchen",
                temperature = 22.5,
            )

            expectThat(EntityActionDomainMapper.invoke(source = request))
                .isA<TargetTemperature.SetTargetTemperature>()
                .with(TargetTemperature.SetTargetTemperature::entity) {
                    isEqualTo(EntityIdentifier("thermostat.kitchen"))
                }
                .with(TargetTemperature.SetTargetTemperature::temperature) {
                    isEqualTo(22.5.celsius)
                }
        }
    }
}
