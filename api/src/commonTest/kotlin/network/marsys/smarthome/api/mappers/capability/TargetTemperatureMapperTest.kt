package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.TargetTemperature
import network.marsys.smarthome.domain.unit.celsius

val TargetTemperatureMapperTest by testSuite(
    name = "Target temperature capability mapper tests",
) {
    testSuite(
        name = "Map target temperature to API model",
    ) {
        test(name = "Mapping succeeds when target temperature is present") {
            val source = TargetTemperature(current = 25.0.celsius)

            expectThat(TargetTemperatureApiMapper.invoke(source))
                .isEqualTo(25.0)
        }
    }

    testSuite(
        name = "Map API model to target temperature",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = 25.0

            expectThat(TargetTemperatureDomainMapper.invoke(source))
                .isA<TargetTemperature>()
                .get(TargetTemperature::current)
                .isEqualTo(25.0.celsius)
        }
    }
}
