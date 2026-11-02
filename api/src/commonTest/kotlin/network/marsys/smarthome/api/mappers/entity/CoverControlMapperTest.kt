package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.hasMessage
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.CoverControlResponse
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.Movement.Direction.Idle
import network.marsys.smarthome.domain.capability.Opened
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.entity.Cover
import network.marsys.smarthome.domain.unit.percent

val CoverControlMapperTest by testSuite(
    name = "Cover control mapper tests",
) {
    testSuite(
        name = "Map cover control to API model",
    ) {
        test(name = "Mapping cover control with position succeeds - all capabilities present") {
            val source = Cover.Control.WithPosition(
                position = required(Position(current = 50.percent)),
                movement = optional(Movement(current = Idle)),
            )

            expectThat(CoverControlApiMapper.invoke(source))
                .with(CoverControlResponse::movement) { isEqualTo(Idle.name) }
                .with(CoverControlResponse::opened) { isNull() }
                .with(CoverControlResponse::position) { isEqualTo(50.0) }
        }

        test(name = "Mapping cover control with position succeeds - only required capabilities present") {
            val source = Cover.Control.WithPosition(
                position = required(Position(current = 50.percent)),
                movement = optional(null),
            )

            expectThat(CoverControlApiMapper.invoke(source))
                .with(CoverControlResponse::movement) { isNull() }
                .with(CoverControlResponse::opened) { isNull() }
                .with(CoverControlResponse::position) { isEqualTo(50.0) }
        }

        test(name = "Mapping cover control without position succeeds - all capabilities present") {
            val source = Cover.Control.WithoutPosition(
                opened = required(Opened(current = true)),
                movement = optional(Movement(current = Idle)),
            )

            expectThat(CoverControlApiMapper.invoke(source))
                .with(CoverControlResponse::movement) { isEqualTo(Idle.name) }
                .with(CoverControlResponse::opened) { isEqualTo(true) }
                .with(CoverControlResponse::position) { isNull() }
        }

        test(name = "Mapping cover control without position succeeds - only required capabilities present") {
            val source = Cover.Control.WithoutPosition(
                opened = required(Opened(current = true)),
                movement = optional(null),
            )

            expectThat(CoverControlApiMapper.invoke(source))
                .with(CoverControlResponse::movement) { isNull() }
                .with(CoverControlResponse::opened) { isEqualTo(true) }
                .with(CoverControlResponse::position) { isNull() }
        }
    }

    testSuite(
        name = "Map API model to cover control",
    ) {
        test(name = "Mapping cover control with position succeeds - all capabilities present") {
            val source = CoverControlResponse(
                movement = Idle.name,
                opened = null,
                position = 50.0,
            )

            expectThat(CoverControlDomainMapper.invoke(source))
                .isA<Cover.Control.WithPosition>()
                .with(Cover.Control.WithPosition::movement) { isA<Capability.Available<*>>() }
                .with(Cover.Control.WithPosition::opened) { isA<Capability.Computed<*>>() }
                .with(Cover.Control.WithPosition::position) { isA<Capability.Required<*>>() }
        }

        test(name = "Mapping cover control with position succeeds - only required capabilities present") {
            val source = CoverControlResponse(
                movement = null,
                opened = null,
                position = 50.0,
            )

            expectThat(CoverControlDomainMapper.invoke(source))
                .isA<Cover.Control.WithPosition>()
                .with(Cover.Control.WithPosition::movement) { isA<Capability.Unsupported>() }
                .with(Cover.Control.WithPosition::opened) { isA<Capability.Computed<*>>() }
                .with(Cover.Control.WithPosition::position) { isA<Capability.Required<*>>() }
        }

        test(name = "Mapping cover control without position succeeds - all capabilities present") {
            val source = CoverControlResponse(
                movement = Idle.name,
                opened = true,
                position = null,
            )

            expectThat(CoverControlDomainMapper.invoke(source))
                .isA<Cover.Control.WithoutPosition>()
                .with(Cover.Control.WithoutPosition::movement) { isA<Capability.Available<*>>() }
                .with(Cover.Control.WithoutPosition::opened) { isA<Capability.Required<*>>() }
                .with(Cover.Control.WithoutPosition::position) { isA<Capability.Unsupported>() }
        }

        test(name = "Mapping cover control without position succeeds - only required capabilities present") {
            val source = CoverControlResponse(
                movement = null,
                opened = true,
                position = null,
            )

            expectThat(CoverControlDomainMapper.invoke(source))
                .isA<Cover.Control.WithoutPosition>()
                .with(Cover.Control.WithoutPosition::movement) { isA<Capability.Unsupported>() }
                .with(Cover.Control.WithoutPosition::opened) { isA<Capability.Required<*>>() }
                .with(Cover.Control.WithoutPosition::position) { isA<Capability.Unsupported>() }
        }

        test(name = "Mapping cover control fails when both position and opened capabilities are present") {
            val source = CoverControlResponse(
                movement = null,
                opened = true,
                position = 50.0,
            )

            expectThrows<IllegalStateException> { CoverControlDomainMapper.invoke(source) }
                .hasMessage("Invalid cover control response: both position and opened cannot be present")
        }

        test(name = "Mapping cover control fails when all capabilities are missing") {
            val source = CoverControlResponse(
                movement = null,
                opened = null,
                position = null,
            )

            expectThrows<IllegalStateException> { CoverControlDomainMapper.invoke(source) }
                .hasMessage("Invalid cover control response: either position or opened must be present")
        }
    }
}
