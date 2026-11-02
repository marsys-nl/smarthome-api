package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.BlindEntityResponse
import network.marsys.smarthome.api.models.entity.CoverControlResponse
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.Movement.Direction
import network.marsys.smarthome.domain.capability.Opened
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.entity.Blind
import network.marsys.smarthome.domain.entity.Cover
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.naming
import network.marsys.smarthome.domain.unit.percent

val BlindMapperTest by testSuite(
    name = "Blind entity mapper tests",
) {
    testSuite(
        name = "Map blind entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = Blind(
                identifier = EntityIdentifier("blind.living-room"),
                naming = Blind.naming(),
                state = Blind.State.Known(
                    control = Cover.Control.WithPosition(
                        position = required(Position(current = 50.percent)),
                        movement = optional(Movement(current = Direction.Idle)),
                    ),
                ),
            )

            expectThat(BlindApiMapper.invoke(source))
                .get(BlindEntityResponse::state)
                .isA<BlindEntityResponse.State>()
        }

        test(name = "Mapping succeeds with known state - with position") {
            val source = Blind(
                identifier = EntityIdentifier("blind.living-room"),
                naming = Blind.naming(),
                state = Blind.State.Known(
                    control = Cover.Control.WithPosition(
                        position = required(Position(current = 50.percent)),
                        movement = optional(Movement(current = Direction.Idle)),
                    ),
                ),
            )

            expectThat(BlindApiMapper.invoke(source))
                .get(BlindEntityResponse::state)
                .isA<BlindEntityResponse.State>()
        }

        test(name = "Mapping succeeds with known state - without position") {
            val source = Blind(
                identifier = EntityIdentifier("blind.living-room"),
                naming = Blind.naming(),
                state = Blind.State.Known(
                    control = Cover.Control.WithoutPosition(
                        opened = required(Opened(current = true)),
                        movement = optional(Movement(current = Direction.Idle)),
                    ),
                ),
            )

            expectThat(BlindApiMapper.invoke(source))
                .get(BlindEntityResponse::state)
                .isA<BlindEntityResponse.State>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = Blind(
                identifier = EntityIdentifier("blind.living-room"),
                naming = Blind.naming(),
                state = Blind.State.Unknown,
            )

            expectThat(BlindApiMapper.invoke(source))
                .get(BlindEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to blind entity",
    ) {
        test(name = "Mapping succeeds with known state - with position") {
            val source = BlindEntityResponse(
                identifier = "blind.living-room",
                naming = EntityResponse.Naming(key = "blind.living-room"),
                state = BlindEntityResponse.State(
                    control = CoverControlResponse(
                        movement = Direction.Idle.name,
                        opened = null,
                        position = 50.0,
                    ),
                ),
            )

            expectThat(BlindDomainMapper.invoke(source))
                .get(Blind::state)
                .isA<Blind.State.Known>()
        }

        test(name = "Mapping succeeds with known state - without position") {
            val source = BlindEntityResponse(
                identifier = "blind.living-room",
                naming = EntityResponse.Naming(key = "blind.living-room"),
                state = BlindEntityResponse.State(
                    control = CoverControlResponse(
                        movement = Direction.Idle.name,
                        opened = true,
                        position = null,
                    ),
                ),
            )

            expectThat(BlindDomainMapper.invoke(source))
                .get(Blind::state)
                .isA<Blind.State.Known>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = BlindEntityResponse(
                identifier = "blind.living-room",
                naming = EntityResponse.Naming(key = "blind.living-room"),
                state = null,
            )

            expectThat(BlindDomainMapper.invoke(source))
                .get(Blind::state)
                .isA<Blind.State.Unknown>()
        }
    }
}
