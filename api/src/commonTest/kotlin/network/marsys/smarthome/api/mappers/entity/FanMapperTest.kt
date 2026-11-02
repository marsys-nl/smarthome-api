package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.api.models.entity.FanEntityResponse
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Fan
import network.marsys.smarthome.domain.entity.naming

val FanMapperTest by testSuite(
    name = "Fan entity mapper tests",
) {
    testSuite(
        name = "Map fan entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = Fan(
                identifier = EntityIdentifier("fan.living-room"),
                naming = Fan.naming(),
                state = Fan.State.Known(
                    onOff = required(OnOff(current = true)),
                ),
            )

            expectThat(FanApiMapper.invoke(source))
                .get(FanEntityResponse::state)
                .isA<FanEntityResponse.State>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = Fan(
                identifier = EntityIdentifier("fan.living-room"),
                naming = Fan.naming(),
                state = Fan.State.Unknown,
            )

            expectThat(FanApiMapper.invoke(source))
                .get(FanEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to fan entity",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = FanEntityResponse(
                identifier = "fan.living-room",
                naming = EntityResponse.Naming(key = "fan.living-room"),
                state = FanEntityResponse.State(
                    onOff = true,
                ),
            )

            expectThat(FanDomainMapper.invoke(source))
                .get(Fan::state)
                .isA<Fan.State.Known>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = FanEntityResponse(
                identifier = "fan.living-room",
                naming = EntityResponse.Naming(key = "fan.living-room"),
                state = null,
            )

            expectThat(FanDomainMapper.invoke(source))
                .get(Fan::state)
                .isA<Fan.State.Unknown>()
        }
    }
}
