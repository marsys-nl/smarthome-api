package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.api.models.entity.SmartLockEntityResponse
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.SmartLock
import network.marsys.smarthome.domain.entity.naming

val SmartLockMapperTest by testSuite(
    name = "SmartLock entity mapper tests",
) {
    testSuite(
        name = "Map smart lock entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = SmartLock(
                identifier = EntityIdentifier("smart-lock.front-door"),
                naming = SmartLock.naming(),
                state = SmartLock.State.Known(
                    onOff = required(OnOff(current = true)),
                ),
            )

            expectThat(SmartLockApiMapper.invoke(source))
                .get(SmartLockEntityResponse::state)
                .isA<SmartLockEntityResponse.State>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = SmartLock(
                identifier = EntityIdentifier("smart-lock.front-door"),
                naming = SmartLock.naming(),
                state = SmartLock.State.Unknown,
            )

            expectThat(SmartLockApiMapper.invoke(source))
                .get(SmartLockEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to smart lock entity",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = SmartLockEntityResponse(
                identifier = "smart-lock.front-door",
                naming = EntityResponse.Naming(key = "smart-lock.front-door"),
                state = SmartLockEntityResponse.State(
                    onOff = true,
                ),
            )

            expectThat(SmartLockDomainMapper.invoke(source))
                .get(SmartLock::state)
                .isA<SmartLock.State.Known>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = SmartLockEntityResponse(
                identifier = "smart-lock.front-door",
                naming = EntityResponse.Naming(key = "smart-lock.front-door"),
                state = null,
            )

            expectThat(SmartLockDomainMapper.invoke(source))
                .get(SmartLock::state)
                .isA<SmartLock.State.Unknown>()
        }
    }
}
