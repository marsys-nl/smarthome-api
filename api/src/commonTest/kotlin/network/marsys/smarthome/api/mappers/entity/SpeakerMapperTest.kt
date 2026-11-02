package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.api.models.entity.SpeakerEntityResponse
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Speaker
import network.marsys.smarthome.domain.entity.naming

val SpeakerMapperTest by testSuite(
    name = "Speaker entity mapper tests",
) {
    testSuite(
        name = "Map speaker entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = Speaker(
                identifier = EntityIdentifier("speaker.living-room"),
                naming = Speaker.naming(),
                state = Speaker.State.Known(
                    onOff = required(OnOff(current = true)),
                ),
            )

            expectThat(SpeakerApiMapper.invoke(source))
                .get(SpeakerEntityResponse::state)
                .isA<SpeakerEntityResponse.State>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = Speaker(
                identifier = EntityIdentifier("speaker.living-room"),
                naming = Speaker.naming(),
                state = Speaker.State.Unknown,
            )

            expectThat(SpeakerApiMapper.invoke(source))
                .get(SpeakerEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to speaker entity",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = SpeakerEntityResponse(
                identifier = "speaker.living-room",
                naming = EntityResponse.Naming(key = "speaker.living-room"),
                state = SpeakerEntityResponse.State(
                    onOff = true,
                ),
            )

            expectThat(SpeakerDomainMapper.invoke(source))
                .get(Speaker::state)
                .isA<Speaker.State.Known>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = SpeakerEntityResponse(
                identifier = "speaker.living-room",
                naming = EntityResponse.Naming(key = "speaker.living-room"),
                state = null,
            )

            expectThat(SpeakerDomainMapper.invoke(source))
                .get(Speaker::state)
                .isA<Speaker.State.Unknown>()
        }
    }
}
