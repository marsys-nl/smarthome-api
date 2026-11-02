package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.WindowDetection

val WindowDetectionMapperTest by testSuite(
    name = "Window detection capability mapper tests",
) {
    testSuite(
        name = "Map window detection to API model",
    ) {
        test(name = "Mapping succeeds when window detection is present") {
            val source = WindowDetection(current = true)

            expectThat(WindowDetectionApiMapper.invoke(source))
                .isEqualTo(true)
        }
    }

    testSuite(
        name = "Map API model to window detection",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = true

            expectThat(WindowDetectionDomainMapper.invoke(source))
                .isA<WindowDetection>()
                .get(WindowDetection::current)
                .isEqualTo(source)
        }
    }
}
