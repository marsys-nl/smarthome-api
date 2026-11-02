package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.MeasureDataSize
import network.marsys.smarthome.domain.unit.gigabytes

val MeasureDataSizeMapperTest by testSuite(
    name = "Measure data size capability mapper tests",
) {
    testSuite(
        name = "Map measure data size to API model",
    ) {
        test(name = "Mapping succeeds when measure data size is present") {
            val source = MeasureDataSize(current = 50.0.gigabytes)

            expectThat(MeasureDataSizeApiMapper.invoke(source))
                .isEqualTo(5.0e10)
        }
    }

    testSuite(
        name = "Map API model to measure data size",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = 5.0e10

            expectThat(MeasureDataSizeDomainMapper.invoke(source))
                .isA<MeasureDataSize>()
                .get(MeasureDataSize::current)
                .isEqualTo(50.0.gigabytes)
        }
    }
}
