package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import network.marsys.smarthome.domain.capability.ChildLock

val ChildLockMapperTest by testSuite(
    name = "Child lock capability mapper tests",
) {
    testSuite(
        name = "Map child lock to API model",
    ) {
        test(name = "Mapping succeeds when child lock is present") {
            val source = ChildLock(current = true)

            expectThat(ChildLockApiMapper.invoke(source))
                .isEqualTo(true)
        }
    }

    testSuite(
        name = "Map API model to child lock",
    ) {
        test(name = "Mapping succeeds when value is present") {
            val source = true

            expectThat(ChildLockDomainMapper.invoke(source))
                .isA<ChildLock>()
                .get(ChildLock::current)
                .isEqualTo(source)
        }
    }
}
