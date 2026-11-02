package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.Opened

val OpenedMapperTest by testSuite(
    name = "Opened capability mapper tests",
) {
    testSuite(
        name = "Map opened to API model",
    ) {
        test(name = "Mapping succeeds when opened is present") {
            val source = Opened(current = true)

            expectThat(OpenedApiMapper.invoke(source))
                .isEqualTo(true)
        }
    }

    testSuite(
        name = "Map API model to opened",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = true

            expectThat(OpenedDomainMapper.invoke(source))
                .isA<Opened>()
                .get(Opened::current)
                .isEqualTo(source)
        }
    }
}
