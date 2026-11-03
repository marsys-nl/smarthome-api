package network.marsys.smarthome.api.mappers

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.contains
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.map
import network.marsys.smarthome.api.models.entity.BlindEntityResponse
import network.marsys.smarthome.api.models.entity.CameraEntityResponse
import network.marsys.smarthome.api.models.entity.CurtainEntityResponse
import network.marsys.smarthome.api.models.entity.FanEntityResponse
import network.marsys.smarthome.api.models.entity.LightEntityResponse
import network.marsys.smarthome.api.models.entity.ShutterEntityResponse
import network.marsys.smarthome.api.models.entity.SmartLockEntityResponse
import network.marsys.smarthome.api.models.entity.SmartPlugEntityResponse
import network.marsys.smarthome.api.models.entity.SpeakerEntityResponse
import network.marsys.smarthome.api.models.entity.SystemEntityResponse
import network.marsys.smarthome.api.models.entity.ThermostatEntityResponse
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.ChildLock
import network.marsys.smarthome.domain.capability.MeasureDataSize
import network.marsys.smarthome.domain.capability.MeasureLoad
import network.marsys.smarthome.domain.capability.MeasureTemperature
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.Movement.Direction
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.capability.Opened
import network.marsys.smarthome.domain.capability.Position
import network.marsys.smarthome.domain.capability.ScheduledMode
import network.marsys.smarthome.domain.capability.TargetTemperature
import network.marsys.smarthome.domain.capability.ThermostatMode
import network.marsys.smarthome.domain.capability.ThermostatMode.Mode
import network.marsys.smarthome.domain.capability.WindowDetection
import network.marsys.smarthome.domain.entity.Blind
import network.marsys.smarthome.domain.entity.Camera
import network.marsys.smarthome.domain.entity.Cover
import network.marsys.smarthome.domain.entity.Curtain
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.Fan
import network.marsys.smarthome.domain.entity.Light
import network.marsys.smarthome.domain.entity.Shutter
import network.marsys.smarthome.domain.entity.SmartLock
import network.marsys.smarthome.domain.entity.SmartPlug
import network.marsys.smarthome.domain.entity.Speaker
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.domain.entity.Thermostat
import network.marsys.smarthome.domain.entity.naming
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.gigabytes
import network.marsys.smarthome.domain.unit.percent
import kotlin.time.Instant

private val entities = listOf(
    Blind(
        identifier = EntityIdentifier("blind.living-room"),
        naming = Blind.naming(),
        state = Blind.State.Known(
            control = Cover.Control.WithPosition(
                position = required(Position(current = 50.percent)),
                movement = optional(Movement(current = Direction.Idle)),
            ),
        ),
    ),
    Camera(
        identifier = EntityIdentifier("camera.living-room"),
        naming = Camera.naming(),
        state = Camera.State.Known(
            onOff = required(OnOff(current = true)),
        ),
    ),
    Curtain(
        identifier = EntityIdentifier("curtain.living-room"),
        naming = Curtain.naming(),
        state = Curtain.State.Known(
            control = Cover.Control.WithPosition(
                position = required(Position(current = 50.percent)),
                movement = optional(Movement(current = Direction.Idle)),
            ),
        ),
    ),
    Fan(
        identifier = EntityIdentifier("fan.living-room"),
        naming = Fan.naming(),
        state = Fan.State.Known(
            onOff = required(OnOff(current = true)),
        ),
    ),
    Light(
        identifier = EntityIdentifier("light.living-room"),
        naming = Light.naming(),
        state = Light.State.Known(
            onOff = required(OnOff(current = true)),
            brightness = optional(Brightness(current = 50.0.percent)),
        ),
    ),
    Shutter(
        identifier = EntityIdentifier("shutter.living-room"),
        naming = Shutter.naming(),
        state = Shutter.State.Known(
            control = Cover.Control.WithoutPosition(
                opened = required(Opened(current = true)),
                movement = optional(Movement(current = Direction.Idle)),
            ),
        ),
    ),
    SmartLock(
        identifier = EntityIdentifier("smart-lock.front-door"),
        naming = SmartLock.naming(),
        state = SmartLock.State.Known(
            onOff = required(OnOff(current = true)),
        ),
    ),
    SmartPlug(
        identifier = EntityIdentifier("smart-plug.office"),
        naming = SmartPlug.naming(),
        state = SmartPlug.State.Known(
            onOff = required(OnOff(current = true)),
        ),
    ),
    Speaker(
        identifier = EntityIdentifier("speaker.living-room"),
        naming = Speaker.naming(),
        state = Speaker.State.Known(
            onOff = required(OnOff(current = true)),
        ),
    ),
    System(
        identifier = EntityIdentifier("system.smarthome"),
        naming = System.naming(),
        state = System.State.Known(
            host = System.Host(
                device = System.Host.Device(
                    manufacturer = "Marsys",
                    model = "SmartHome Hub",
                    architecture = "x86_64",
                    physicalCores = 4,
                    logicalCores = 8,
                ),
                operatingSystem = System.Host.OperatingSystem(
                    description = "Ubuntu 20.04 LTS",
                    family = "Linux",
                    version = "20.04",
                    bitness = 64,
                ),
            ),
            processor = System.Processor(
                load = required(MeasureLoad(current = 50.percent)),
                temperature = optional(MeasureTemperature(current = 65.0.celsius)),
            ),
            memory = System.Memory(
                total = required(MeasureDataSize(current = 8.0.gigabytes)),
                available = required(MeasureDataSize(current = 4.0.gigabytes)),
                swap = System.Memory.Swap(
                    total = required(MeasureDataSize(current = 2.0.gigabytes)),
                    available = required(MeasureDataSize(current = 1.0.gigabytes)),
                ),
            ),
            startedAt = System.StartedAt(
                host = Instant.parse("2026-09-01T00:00:00Z"),
                application = Instant.parse("2026-09-01T00:01:00Z"),
            ),
        ),
    ),
    Thermostat(
        identifier = EntityIdentifier("thermostat.living-room"),
        naming = Thermostat.naming(),
        state = Thermostat.State.Known(
            onOff = required(OnOff(current = true)),
            mode = required(ThermostatMode(current = Mode.Auto)),
            temperatures = Thermostat.Temperatures(
                target = required(TargetTemperature(current = 22.0.celsius)),
                current = required(MeasureTemperature(current = 21.0.celsius)),
                outdoor = optional(MeasureTemperature(current = 15.0.celsius)),
            ),
            childLock = optional(ChildLock(current = false)),
            windowDetection = optional(WindowDetection(current = true)),
            scheduledMode = optional(ScheduledMode(current = false)),
        ),
    ),
)

val EntityMapperTest by testSuite(
    name = "Entity mapper tests",
) {
    test(name = "Mapping entity collection to API model") {
        expectThat(EntityCollectionApiMapper.invoke(source = entities))
            .with(Collection<*>::size) { isEqualTo(entities.size) }
            .map { it::class }
            .contains(
                BlindEntityResponse::class,
                CameraEntityResponse::class,
                CurtainEntityResponse::class,
                FanEntityResponse::class,
                LightEntityResponse::class,
                ShutterEntityResponse::class,
                SmartLockEntityResponse::class,
                SmartPlugEntityResponse::class,
                SpeakerEntityResponse::class,
                SystemEntityResponse::class,
                ThermostatEntityResponse::class,
            )
    }

    test(name = "Mapping API models back to entity collection") {
        val mapped = EntityCollectionApiMapper.invoke(source = entities)

        expectThat(EntityCollectionDomainMapper.invoke(source = mapped))
            .with(Collection<*>::size) { isEqualTo(entities.size) }
            .isEqualTo(entities)
    }
}
