package network.marsys.smarthome.api.mappers.entity

import network.marsys.smarthome.api.mappers.EntityIdentifierApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingApiMapper
import network.marsys.smarthome.api.mappers.EntityNamingDomainMapper
import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintApiMapper
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.optional
import network.marsys.smarthome.api.mappers.capability.CapabilityConstraintDomainMapper.required
import network.marsys.smarthome.api.mappers.capability.MeasureDataSizeApiMapper
import network.marsys.smarthome.api.mappers.capability.MeasureDataSizeDomainMapper
import network.marsys.smarthome.api.mappers.capability.MeasureLoadApiMapper
import network.marsys.smarthome.api.mappers.capability.MeasureLoadDomainMapper
import network.marsys.smarthome.api.mappers.capability.MeasureTemperatureApiMapper
import network.marsys.smarthome.api.mappers.capability.MeasureTemperatureDomainMapper
import network.marsys.smarthome.api.models.entity.SystemEntityResponse
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.entity.System

object SystemApiMapper : Mapper<System, SystemEntityResponse> {
    override fun invoke(source: System) = SystemEntityResponse(
        identifier = EntityIdentifierApiMapper.invoke(source.identifier),
        naming = EntityNamingApiMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is System.State.Known -> map(state = state)
            else -> null
        },
    )

    private fun map(state: System.State.Known) = SystemEntityResponse.State(
        host = SystemEntityResponse.Host(
            device = SystemEntityResponse.Device(
                manufacturer = state.host.device.manufacturer,
                model = state.host.device.model,
                architecture = state.host.device.architecture,
                physicalCores = state.host.device.physicalCores,
                logicalCores = state.host.device.logicalCores,
            ),
            operatingSystem = SystemEntityResponse.OperatingSystem(
                description = state.host.operatingSystem.description,
                family = state.host.operatingSystem.family,
                version = state.host.operatingSystem.version,
                bitness = state.host.operatingSystem.bitness,
            ),
        ),
        processor = SystemEntityResponse.Processor(
            load = CapabilityConstraintApiMapper
                .invoke(source = state.processor.load, mapper = MeasureLoadApiMapper),
            temperature = CapabilityConstraintApiMapper
                .invoke(source = state.processor.temperature, mapper = MeasureTemperatureApiMapper),
        ),
        memory = SystemEntityResponse.Memory(
            total = CapabilityConstraintApiMapper
                .invoke(source = state.memory.total, mapper = MeasureDataSizeApiMapper),
            available = CapabilityConstraintApiMapper
                .invoke(source = state.memory.available, mapper = MeasureDataSizeApiMapper),
            swap = SystemEntityResponse.Swap(
                total = CapabilityConstraintApiMapper
                    .invoke(source = state.memory.swap.total, mapper = MeasureDataSizeApiMapper),
                available = CapabilityConstraintApiMapper
                    .invoke(source = state.memory.swap.available, mapper = MeasureDataSizeApiMapper),
            ),
        ),
        startedAt = SystemEntityResponse.StartedAt(
            application = state.startedAt.application,
            host = state.startedAt.host,
        ),
    )
}

object SystemDomainMapper : Mapper<SystemEntityResponse, System> {
    override fun invoke(source: SystemEntityResponse) = System(
        identifier = EntityIdentifier(value = source.identifier),
        naming = EntityNamingDomainMapper.invoke(source.naming),
        state = when (val state = source.state) {
            is SystemEntityResponse.State -> map(state = state)
            else -> System.State.Unknown
        },
    )

    private fun map(state: SystemEntityResponse.State) = System.State.Known(
        host = System.Host(
            device = System.Host.Device(
                manufacturer = state.host.device.manufacturer,
                model = state.host.device.model,
                architecture = state.host.device.architecture,
                physicalCores = state.host.device.physicalCores,
                logicalCores = state.host.device.logicalCores,
            ),
            operatingSystem = System.Host.OperatingSystem(
                description = state.host.operatingSystem.description,
                family = state.host.operatingSystem.family,
                version = state.host.operatingSystem.version,
                bitness = state.host.operatingSystem.bitness,
            ),
        ),
        processor = System.Processor(
            load = required(source = state.processor.load, mapper = MeasureLoadDomainMapper),
            temperature = optional(source = state.processor.temperature, mapper = MeasureTemperatureDomainMapper),
        ),
        memory = System.Memory(
            total = required(source = state.memory.total, mapper = MeasureDataSizeDomainMapper),
            available = required(source = state.memory.available, mapper = MeasureDataSizeDomainMapper),
            swap = System.Memory.Swap(
                total = required(source = state.memory.swap.total, mapper = MeasureDataSizeDomainMapper),
                available = required(source = state.memory.swap.available, mapper = MeasureDataSizeDomainMapper),
            ),
        ),
        startedAt = System.StartedAt(
            application = state.startedAt.application,
            host = state.startedAt.host,
        ),
    )
}
