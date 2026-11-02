package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.MeasureTemperature
import network.marsys.smarthome.domain.unit.celsius

val MeasureTemperatureMapperTest by testSuite(
    name = "Measure temperature capability mapper tests",
) {
    testSuite(
        name = "Map measure temperature to API model",
    ) {
        test(name = "Mapping succeeds when measure temperature is present") {
            val source = MeasureTemperature(current = 25.0.celsius)

            expectThat(MeasureTemperatureApiMapper.invoke(source))
                .isEqualTo(25.0)
        }
    }

    testSuite(
        name = "Map API model to measure temperature",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = 25.0

            expectThat(MeasureTemperatureDomainMapper.invoke(source))
                .isA<MeasureTemperature>()
                .get(MeasureTemperature::current)
                .isEqualTo(25.0.celsius)
        }
    }
}
