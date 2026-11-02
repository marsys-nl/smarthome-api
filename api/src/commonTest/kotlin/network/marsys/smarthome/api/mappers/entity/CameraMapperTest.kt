package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.CameraEntityResponse
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.entity.Camera
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.naming

val CameraMapperTest by testSuite(
    name = "Camera entity mapper tests",
) {
    testSuite(
        name = "Map camera entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = Camera(
                identifier = EntityIdentifier("camera.living-room"),
                naming = Camera.naming(),
                state = Camera.State.Known(
                    onOff = required(OnOff(current = true)),
                ),
            )

            expectThat(CameraApiMapper.invoke(source))
                .get(CameraEntityResponse::state)
                .isA<CameraEntityResponse.State>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = Camera(
                identifier = EntityIdentifier("camera.living-room"),
                naming = Camera.naming(),
                state = Camera.State.Unknown,
            )

            expectThat(CameraApiMapper.invoke(source))
                .get(CameraEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to camera entity",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = CameraEntityResponse(
                identifier = "camera.living-room",
                naming = EntityResponse.Naming(key = "camera.living-room"),
                state = CameraEntityResponse.State(
                    onOff = true,
                ),
            )

            expectThat(CameraDomainMapper.invoke(source))
                .get(Camera::state)
                .isA<Camera.State.Known>()
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = CameraEntityResponse(
                identifier = "camera.living-room",
                naming = EntityResponse.Naming(key = "camera.living-room"),
                state = null,
            )

            expectThat(CameraDomainMapper.invoke(source))
                .get(Camera::state)
                .isA<Camera.State.Unknown>()
        }
    }
}
