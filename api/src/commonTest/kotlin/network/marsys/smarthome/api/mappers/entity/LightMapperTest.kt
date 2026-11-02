package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNotNull
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.api.models.entity.LightEntityResponse
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Light
import network.marsys.smarthome.domain.entity.naming
import network.marsys.smarthome.domain.unit.percent

val LightMapperTest by testSuite(
    name = "Light entity mapper tests",
) {
    testSuite(
        name = "Map light entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = Light(
                identifier = EntityIdentifier("light.living-room"),
                naming = Light.naming(),
                state = Light.State.Known(
                    onOff = required(OnOff(current = true)),
                    brightness = optional(Brightness(current = 50.0.percent)),
                ),
            )

            expectThat(LightApiMapper.invoke(source))
                .get(LightEntityResponse::state)
                .isA<LightEntityResponse.State>()
        }

        test(name = "Mapping succeeds with known state - all capabilities present") {
            val source = Light(
                identifier = EntityIdentifier("light.living-room"),
                naming = Light.naming(),
                state = Light.State.Known(
                    onOff = required(OnOff(current = true)),
                    brightness = optional(Brightness(current = 50.0.percent)),
                ),
            )

            expectThat(LightApiMapper.invoke(source))
                .get(LightEntityResponse::state)
                .isA<LightEntityResponse.State>()
                .with(LightEntityResponse.State::brightness) { isNotNull() }
        }

        test(name = "Mapping succeeds with known state - only required capabilities present") {
            val source = Light(
                identifier = EntityIdentifier("light.living-room"),
                naming = Light.naming(),
                state = Light.State.Known(
                    onOff = required(OnOff(current = true)),
                    brightness = optional(null),
                ),
            )

            expectThat(LightApiMapper.invoke(source))
                .get(LightEntityResponse::state)
                .isA<LightEntityResponse.State>()
                .with(LightEntityResponse.State::brightness) { isNull() }
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = Light(
                identifier = EntityIdentifier("light.living-room"),
                naming = Light.naming(),
                state = Light.State.Unknown,
            )

            expectThat(LightApiMapper.invoke(source))
                .get(LightEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to light entity",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = LightEntityResponse(
                identifier = "light.living-room",
                naming = EntityResponse.Naming(key = "light.living-room"),
                state = LightEntityResponse.State(
                    onOff = true,
                    brightness = 50.0,
                ),
            )

            expectThat(LightDomainMapper.invoke(source))
                .get(Light::state)
                .isA<Light.State.Known>()
        }

        test(name = "Mapping succeeds with known state - all capabilities present") {
            val source = LightEntityResponse(
                identifier = "light.living-room",
                naming = EntityResponse.Naming(key = "light.living-room"),
                state = LightEntityResponse.State(
                    onOff = true,
                    brightness = 50.0,
                ),
            )

            expectThat(LightDomainMapper.invoke(source))
                .get(Light::state)
                .isA<Light.State.Known>()
                .with(Light.State.Known::brightness) { isA<Capability.Available<Brightness>>() }
        }

        test(name = "Mapping succeeds with known state - only required capabilities present") {
            val source = LightEntityResponse(
                identifier = "light.living-room",
                naming = EntityResponse.Naming(key = "light.living-room"),
                state = LightEntityResponse.State(
                    onOff = true,
                    brightness = null,
                ),
            )

            expectThat(LightDomainMapper.invoke(source))
                .get(Light::state)
                .isA<Light.State.Known>()
                .with(Light.State.Known::brightness) { isA<Capability.Unsupported>() }
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = LightEntityResponse(
                identifier = "light.living-room",
                naming = EntityResponse.Naming(key = "light.living-room"),
                state = null,
            )

            expectThat(LightDomainMapper.invoke(source))
                .get(Light::state)
                .isA<Light.State.Unknown>()
        }
    }
}
