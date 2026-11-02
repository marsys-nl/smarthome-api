package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.MeasureLoad
import network.marsys.smarthome.domain.unit.percent

val MeasureLoadMapperTest by testSuite(
    name = "Measure load capability mapper tests",
) {
    testSuite(
        name = "Map measure load to API model",
    ) {
        test(name = "Mapping succeeds when measure load is present") {
            val source = MeasureLoad(current = 50.0.percent)

            expectThat(MeasureLoadApiMapper.invoke(source))
                .isEqualTo(50.0)
        }
    }

    testSuite(
        name = "Map API model to measure load",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = 50.0

            expectThat(MeasureLoadDomainMapper.invoke(source))
                .isA<MeasureLoad>()
                .get(MeasureLoad::current)
                .isEqualTo(50.0.percent)
        }
    }
}
