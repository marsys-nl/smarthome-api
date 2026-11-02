package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.CoverControlResponse
import network.marsys.smarthome.api.models.entity.CurtainEntityResponse
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.Movement.Direction
import network.marsys.smarthome.domain.capability.Opened
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.entity.Cover
import network.marsys.smarthome.domain.entity.Curtain
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.naming
import network.marsys.smarthome.domain.unit.percent

val CurtainMapperTest by testSuite(
    name = "Curtain entity mapper tests",
) {
    testSuite(
        name = "Map curtain entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = Curtain(
                identifier = EntityIdentifier("curtain.living-room"),
                naming = Curtain.naming(),
                state = Curtain.State.Known(
                    control = Cover.Control.WithPosition(
                        position = required(Position(current = 50.percent)),
                        movement = optional(Movement(current = Direction.Idle)),
                    ),
                ),
            )

            expectThat(CurtainApiMapper.invoke(source))
                .get(CurtainEntityResponse::state)
                .isA<CurtainEntityResponse.State>()
        }

        test(name = "Mapping succeeds with known state - with position") {
            val source = Curtain(
                identifier = EntityIdentifier("curtain.living-room"),
                naming = Curtain.naming(),
                state = Curtain.State.Known(
                    control = Cover.Control.WithPosition(
                        position = required(Position(current = 50.percent)),
                        movement = optional(Movement(current = Direction.Idle)),
                    ),
                ),
            )

            expectThat(CurtainApiMapper.invoke(source))
                .get(CurtainEntityResponse::state)
                .isA<CurtainEntityResponse.State>()
        }

        test(name = "Mapping succeeds with known state - without position") {
            val source = Curtain(
                identifier = EntityIdentifier("curtain.living-room"),
                naming = Curtain.naming(),
                state = Curtain.State.Known(
                    control = Cover.Control.WithoutPosition(
                        opened = required(Opened(current = true)),
                        movement = optional(Movement(current = Direction.Idle)),
                    ),
                ),
            )

            expectThat(CurtainApiMapper.invoke(source))
                .get(CurtainEntityResponse::state)
                .isA<CurtainEntityResponse.State>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = Curtain(
                identifier = EntityIdentifier("curtain.living-room"),
                naming = Curtain.naming(),
                state = Curtain.State.Unknown,
            )

            expectThat(CurtainApiMapper.invoke(source))
                .get(CurtainEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to curtain entity",
    ) {
        test(name = "Mapping succeeds with known state - with position") {
            val source = CurtainEntityResponse(
                identifier = "curtain.living-room",
                naming = EntityResponse.Naming(key = "curtain.living-room"),
                state = CurtainEntityResponse.State(
                    control = CoverControlResponse(
                        movement = Direction.Idle.name,
                        opened = null,
                        position = 50.0,
                    ),
                ),
            )

            expectThat(CurtainDomainMapper.invoke(source))
                .get(Curtain::state)
                .isA<Curtain.State.Known>()
        }

        test(name = "Mapping succeeds with known state - without position") {
            val source = CurtainEntityResponse(
                identifier = "curtain.living-room",
                naming = EntityResponse.Naming(key = "curtain.living-room"),
                state = CurtainEntityResponse.State(
                    control = CoverControlResponse(
                        movement = Direction.Idle.name,
                        opened = true,
                        position = null,
                    ),
                ),
            )

            expectThat(CurtainDomainMapper.invoke(source))
                .get(Curtain::state)
                .isA<Curtain.State.Known>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = CurtainEntityResponse(
                identifier = "curtain.living-room",
                naming = EntityResponse.Naming(key = "curtain.living-room"),
                state = null,
            )

            expectThat(CurtainDomainMapper.invoke(source))
                .get(Curtain::state)
                .isA<Curtain.State.Unknown>()
        }
    }
}
