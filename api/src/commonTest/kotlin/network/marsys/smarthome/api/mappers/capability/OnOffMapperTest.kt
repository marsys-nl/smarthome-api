package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.OnOff

val OnOffMapperTest by testSuite(
    name = "OnOff capability mapper tests",
) {
    testSuite(
        name = "Map on-off to API model",
    ) {
        test(name = "Mapping succeeds when on-off is present") {
            val source = OnOff(current = true)

            expectThat(OnOffApiMapper.invoke(source))
                .isEqualTo(true)
        }
    }

    testSuite(
        name = "Map API model to on-off",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = true

            expectThat(OnOffDomainMapper.invoke(source))
                .isA<OnOff>()
                .get(OnOff::current)
                .isEqualTo(source)
        }
    }
}
