package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.api.models.entity.SmartPlugEntityResponse
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.SmartPlug
import network.marsys.smarthome.domain.entity.naming

val SmartPlugMapperTest by testSuite(
    name = "SmartPlug entity mapper tests",
) {
    testSuite(
        name = "Map smart plug entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = SmartPlug(
                identifier = EntityIdentifier("smart-plug.office"),
                naming = SmartPlug.naming(),
                state = SmartPlug.State.Known(
                    onOff = required(OnOff(current = true)),
                ),
            )

            expectThat(SmartPlugApiMapper.invoke(source))
                .get(SmartPlugEntityResponse::state)
                .isA<SmartPlugEntityResponse.State>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = SmartPlug(
                identifier = EntityIdentifier("smart-plug.office"),
                naming = SmartPlug.naming(),
                state = SmartPlug.State.Unknown,
            )

            expectThat(SmartPlugApiMapper.invoke(source))
                .get(SmartPlugEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to smart plug entity",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = SmartPlugEntityResponse(
                identifier = "smart-plug.office",
                naming = EntityResponse.Naming(key = "smart-plug.office"),
                state = SmartPlugEntityResponse.State(
                    onOff = true,
                ),
            )

            expectThat(SmartPlugDomainMapper.invoke(source))
                .get(SmartPlug::state)
                .isA<SmartPlug.State.Known>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = SmartPlugEntityResponse(
                identifier = "smart-plug.office",
                naming = EntityResponse.Naming(key = "smart-plug.office"),
                state = null,
            )

            expectThat(SmartPlugDomainMapper.invoke(source))
                .get(SmartPlug::state)
                .isA<SmartPlug.State.Unknown>()
        }
    }
}
