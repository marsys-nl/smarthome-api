package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.unit.percent

val PositionMapperTest by testSuite(
    name = "Position capability mapper tests",
) {
    testSuite(
        name = "Map position to API model",
    ) {
        test(name = "Mapping succeeds when position is present") {
            val source = Position(current = 50.0.percent)

            expectThat(PositionApiMapper.invoke(source))
                .isEqualTo(50.0)
        }
    }

    testSuite(
        name = "Map API model to position",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = 50.0

            expectThat(PositionDomainMapper.invoke(source))
                .isA<Position>()
                .get(Position::current)
                .isEqualTo(50.0.percent)
        }
    }
}
