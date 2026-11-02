package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.Movement.Direction.Idle

val MovementMapperTest by testSuite(
    name = "Movement capability mapper tests",
) {
    testSuite(
        name = "Map movement to API model",
    ) {
        test(name = "Mapping succeeds when movement is present") {
            val source = Movement(current = Idle)

            expectThat(MovementApiMapper.invoke(source))
                .isEqualTo(Idle.name)
        }
    }

    testSuite(
        name = "Map API model to movement",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = Idle.name

            expectThat(MovementDomainMapper.invoke(source))
                .isA<Movement>()
                .get(Movement::current)
                .isEqualTo(Idle)
        }
    }
}
