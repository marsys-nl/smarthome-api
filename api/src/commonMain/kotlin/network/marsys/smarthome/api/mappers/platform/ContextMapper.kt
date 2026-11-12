package network.marsys.smarthome.api.mappers.platform

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.models.platform.ContextElementMessage
import network.marsys.smarthome.api.models.platform.ContextMessage
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.domain.entity.Thermostat

internal object ContextApiMapper : Mapper<Capability.Context, ContextMessage> {
    override fun invoke(source: Capability.Context): ContextMessage =
        ContextMessage(
            elements = when (source) {
                is Capability.Context.Empty -> emptyList()

                else -> source.fold(
                    initial = emptyList(),
                    operation = { acc, element ->
                        acc + ContextElementApiMapper.invoke(element)
                    },
                )
            },
        )
}

internal object ContextDomainMapper : Mapper<ContextMessage, Capability.Context> {
    override fun invoke(source: ContextMessage): Capability.Context =
        when (source.elements) {
            emptyList<ContextElementMessage>() -> Capability.Context.Empty

            else -> source.elements.fold(
                initial = Capability.Context.Empty as Capability.Context,
                operation = { accumulator, element ->
                    accumulator + ContextElementDomainMapper.invoke(element)
                },
            )
        }
}

private object ContextElementApiMapper : Mapper<Capability.Context.Element, ContextElementMessage> {
    @Suppress("ktlint:standard:blank-line-between-when-conditions")
    override fun invoke(source: Capability.Context.Element): ContextElementMessage =
        when (source) {
            is System.MemoryType.Total -> ContextElementMessage.MemoryType.Total
            is System.MemoryType.Available -> ContextElementMessage.MemoryType.Available
            is System.MemoryType.SwapTotal -> ContextElementMessage.MemoryType.SwapTotal
            is System.MemoryType.SwapAvailable -> ContextElementMessage.MemoryType.SwapAvailable

            is Thermostat.TemperatureType.Current -> ContextElementMessage.TemperatureType.Current
            is Thermostat.TemperatureType.Outdoor -> ContextElementMessage.TemperatureType.Outdoor

            else -> error("Unsupported context element type: ${source::class.simpleName}")
        }
}

private object ContextElementDomainMapper : Mapper<ContextElementMessage, Capability.Context.Element> {
    @Suppress("ktlint:standard:blank-line-between-when-conditions")
    override fun invoke(source: ContextElementMessage): Capability.Context.Element =
        when (source) {
            is ContextElementMessage.MemoryType.Total -> System.MemoryType.Total
            is ContextElementMessage.MemoryType.Available -> System.MemoryType.Available
            is ContextElementMessage.MemoryType.SwapTotal -> System.MemoryType.SwapTotal
            is ContextElementMessage.MemoryType.SwapAvailable -> System.MemoryType.SwapAvailable

            is ContextElementMessage.TemperatureType.Current -> Thermostat.TemperatureType.Current
            is ContextElementMessage.TemperatureType.Outdoor -> Thermostat.TemperatureType.Outdoor
        }
}
