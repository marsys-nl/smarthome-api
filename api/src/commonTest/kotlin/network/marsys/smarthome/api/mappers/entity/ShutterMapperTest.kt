package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.CoverControlResponse
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.api.models.entity.ShutterEntityResponse
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.Movement.Direction
import network.marsys.smarthome.domain.capability.Opened
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.entity.Cover
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Shutter
import network.marsys.smarthome.domain.entity.naming
import network.marsys.smarthome.domain.unit.percent

val ShutterMapperTest by testSuite(
    name = "Shutter entity mapper tests",
) {
    testSuite(
        name = "Map shutter entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = Shutter(
                identifier = EntityIdentifier("shutter.living-room"),
                naming = Shutter.naming(),
                state = Shutter.State.Known(
                    control = Cover.Control.WithPosition(
                        position = required(Position(current = 50.percent)),
                        movement = optional(Movement(current = Direction.Idle)),
                    ),
                ),
            )

            expectThat(ShutterApiMapper.invoke(source))
                .get(ShutterEntityResponse::state)
                .isA<ShutterEntityResponse.State>()
        }

        test(name = "Mapping succeeds with known state - with position") {
            val source = Shutter(
                identifier = EntityIdentifier("shutter.living-room"),
                naming = Shutter.naming(),
                state = Shutter.State.Known(
                    control = Cover.Control.WithPosition(
                        position = required(Position(current = 50.percent)),
                        movement = optional(Movement(current = Direction.Idle)),
                    ),
                ),
            )

            expectThat(ShutterApiMapper.invoke(source))
                .get(ShutterEntityResponse::state)
                .isA<ShutterEntityResponse.State>()
        }

        test(name = "Mapping succeeds with known state - without position") {
            val source = Shutter(
                identifier = EntityIdentifier("shutter.living-room"),
                naming = Shutter.naming(),
                state = Shutter.State.Known(
                    control = Cover.Control.WithoutPosition(
                        opened = required(Opened(current = true)),
                        movement = optional(Movement(current = Direction.Idle)),
                    ),
                ),
            )

            expectThat(ShutterApiMapper.invoke(source))
                .get(ShutterEntityResponse::state)
                .isA<ShutterEntityResponse.State>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = Shutter(
                identifier = EntityIdentifier("shutter.living-room"),
                naming = Shutter.naming(),
                state = Shutter.State.Unknown,
            )

            expectThat(ShutterApiMapper.invoke(source))
                .get(ShutterEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to shutter entity",
    ) {
        test(name = "Mapping succeeds with known state - with position") {
            val source = ShutterEntityResponse(
                identifier = "shutter.living-room",
                naming = EntityResponse.Naming(key = "shutter.living-room"),
                state = ShutterEntityResponse.State(
                    control = CoverControlResponse(
                        movement = Direction.Idle.name,
                        opened = null,
                        position = 50.0,
                    ),
                ),
            )

            expectThat(ShutterDomainMapper.invoke(source))
                .get(Shutter::state)
                .isA<Shutter.State.Known>()
        }

        test(name = "Mapping succeeds with known state - without position") {
            val source = ShutterEntityResponse(
                identifier = "shutter.living-room",
                naming = EntityResponse.Naming(key = "shutter.living-room"),
                state = ShutterEntityResponse.State(
                    control = CoverControlResponse(
                        movement = Direction.Idle.name,
                        opened = true,
                        position = null,
                    ),
                ),
            )

            expectThat(ShutterDomainMapper.invoke(source))
                .get(Shutter::state)
                .isA<Shutter.State.Known>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = ShutterEntityResponse(
                identifier = "shutter.living-room",
                naming = EntityResponse.Naming(key = "shutter.living-room"),
                state = null,
            )

            expectThat(ShutterDomainMapper.invoke(source))
                .get(Shutter::state)
                .isA<Shutter.State.Unknown>()
        }
    }
}
