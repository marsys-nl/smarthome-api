package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.ScheduledMode

val ScheduledModeMapperTest by testSuite(
    name = "Scheduled mode capability mapper tests",
) {
    testSuite(
        name = "Map scheduled mode to API model",
    ) {
        test(name = "Mapping succeeds when scheduled mode is present") {
            val source = ScheduledMode(current = true)

            expectThat(ScheduledModeApiMapper.invoke(source))
                .isEqualTo(true)
        }
    }

    testSuite(
        name = "Map API model to scheduled mode",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = true

            expectThat(ScheduledModeDomainMapper.invoke(source))
                .isA<ScheduledMode>()
                .get(ScheduledMode::current)
                .isEqualTo(source)
        }
    }
}
