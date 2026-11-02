package network.marsys.smarthome.api.mappers.entity

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isNotNull
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.api.models.entity.SystemEntityResponse
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.MeasureDataSize
import network.marsys.smarthome.domain.capability.MeasureLoad
import network.marsys.smarthome.domain.capability.MeasureTemperature
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.domain.entity.naming
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.gigabytes
import network.marsys.smarthome.domain.unit.percent
import kotlin.time.Instant

val SystemMapperTest by testSuite(
    name = "System entity mapper tests",
) {
    testSuite(
        name = "Map system entity to API model",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = System(
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
                        temperature = optional(null),
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
            )

            expectThat(SystemApiMapper.invoke(source))
                .get(SystemEntityResponse::state)
                .isA<SystemEntityResponse.State>()
        }

        test(name = "Mapping succeeds with known state - all capabilities present") {
            val source = System(
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
                        temperature = optional(MeasureTemperature(current = 45.0.celsius)),
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
            )

            expectThat(SystemApiMapper.invoke(source))
                .get(SystemEntityResponse::state)
                .isA<SystemEntityResponse.State>()
                .with(SystemEntityResponse.State::processor) {
                    get(SystemEntityResponse.Processor::temperature)
                        .isNotNull()
                }
        }

        test(name = "Mapping succeeds with known state - only required capabilities present") {
            val source = System(
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
                        temperature = optional(null),
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
            )

            expectThat(SystemApiMapper.invoke(source))
                .get(SystemEntityResponse::state)
                .isA<SystemEntityResponse.State>()
                .with(SystemEntityResponse.State::processor) {
                    get(SystemEntityResponse.Processor::temperature)
                        .isNull()
                }
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = System(
                identifier = EntityIdentifier("system.smarthome"),
                naming = System.naming(),
                state = System.State.Unknown,
            )

            expectThat(SystemApiMapper.invoke(source))
                .get(SystemEntityResponse::state)
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to system entity",
    ) {
        test(name = "Mapping succeeds with known state") {
            val source = SystemEntityResponse(
                identifier = "system.smarthome",
                naming = EntityResponse.Naming(key = "system.smarthome"),
                state = SystemEntityResponse.State(
                    host = SystemEntityResponse.Host(
                        device = SystemEntityResponse.Device(
                            manufacturer = "Marsys",
                            model = "SmartHome Hub",
                            architecture = "x86_64",
                            physicalCores = 4,
                            logicalCores = 8,
                        ),
                        operatingSystem = SystemEntityResponse.OperatingSystem(
                            description = "Ubuntu 20.04 LTS",
                            family = "Linux",
                            version = "20.04",
                            bitness = 64,
                        ),
                    ),
                    processor = SystemEntityResponse.Processor(
                        load = 50.0,
                        temperature = null,
                    ),
                    memory = SystemEntityResponse.Memory(
                        total = 8.0,
                        available = 4.0,
                        swap = SystemEntityResponse.Swap(
                            total = 2.0,
                            available = 1.0,
                        ),
                    ),
                    startedAt = SystemEntityResponse.StartedAt(
                        host = Instant.parse("2026-09-01T00:00:00Z"),
                        application = Instant.parse("2026-09-01T00:01:00Z"),
                    ),
                ),
            )

            expectThat(SystemDomainMapper.invoke(source))
                .get(System::state)
                .isA<System.State.Known>()
        }

        test(name = "Mapping succeeds with known state - all capabilities present") {
            val source = SystemEntityResponse(
                identifier = "system.smarthome",
                naming = EntityResponse.Naming(key = "system.smarthome"),
                state = SystemEntityResponse.State(
                    host = SystemEntityResponse.Host(
                        device = SystemEntityResponse.Device(
                            manufacturer = "Marsys",
                            model = "SmartHome Hub",
                            architecture = "x86_64",
                            physicalCores = 4,
                            logicalCores = 8,
                        ),
                        operatingSystem = SystemEntityResponse.OperatingSystem(
                            description = "Ubuntu 20.04 LTS",
                            family = "Linux",
                            version = "20.04",
                            bitness = 64,
                        ),
                    ),
                    processor = SystemEntityResponse.Processor(
                        load = 50.0,
                        temperature = 45.0,
                    ),
                    memory = SystemEntityResponse.Memory(
                        total = 8.0,
                        available = 4.0,
                        swap = SystemEntityResponse.Swap(
                            total = 2.0,
                            available = 1.0,
                        ),
                    ),
                    startedAt = SystemEntityResponse.StartedAt(
                        host = Instant.parse("2026-09-01T00:00:00Z"),
                        application = Instant.parse("2026-09-01T00:01:00Z"),
                    ),
                ),
            )

            expectThat(SystemDomainMapper.invoke(source))
                .get(System::state)
                .isA<System.State.Known>()
                .with(System.State.Known::processor) {
                    get(System.Processor::temperature)
                        .isA<Capability.Available<MeasureTemperature>>()
                }
        }

        test(name = "Mapping succeeds with known state - only required capabilities present") {
            val source = SystemEntityResponse(
                identifier = "system.smarthome",
                naming = EntityResponse.Naming(key = "system.smarthome"),
                state = SystemEntityResponse.State(
                    host = SystemEntityResponse.Host(
                        device = SystemEntityResponse.Device(
                            manufacturer = "Marsys",
                            model = "SmartHome Hub",
                            architecture = "x86_64",
                            physicalCores = 4,
                            logicalCores = 8,
                        ),
                        operatingSystem = SystemEntityResponse.OperatingSystem(
                            description = "Ubuntu 20.04 LTS",
                            family = "Linux",
                            version = "20.04",
                            bitness = 64,
                        ),
                    ),
                    processor = SystemEntityResponse.Processor(
                        load = 50.0,
                        temperature = null,
                    ),
                    memory = SystemEntityResponse.Memory(
                        total = 8.0,
                        available = 4.0,
                        swap = SystemEntityResponse.Swap(
                            total = 2.0,
                            available = 1.0,
                        ),
                    ),
                    startedAt = SystemEntityResponse.StartedAt(
                        host = Instant.parse("2026-09-01T00:00:00Z"),
                        application = Instant.parse("2026-09-01T00:01:00Z"),
                    ),
                ),
            )

            expectThat(SystemDomainMapper.invoke(source))
                .get(System::state)
                .isA<System.State.Known>()
                .with(System.State.Known::processor) {
                    get(System.Processor::temperature)
                        .isA<Capability.Unsupported>()
                }
        }

        test(name = "Mapping succeeds with unknown state") {
            val source = SystemEntityResponse(
                identifier = "system.smarthome",
                naming = EntityResponse.Naming(key = "system.smarthome"),
                state = null,
            )

            expectThat(SystemDomainMapper.invoke(source))
                .get(System::state)
                .isA<System.State.Unknown>()
        }
    }
}
