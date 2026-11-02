package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.Capability

object CapabilityConstraintApiMapper {
    operator fun <C : Capability<*, *>, T : Any> invoke(
        source: Capability.Required<C>,
        mapper: Mapper<C, T>,
    ): T = mapper(source.value)

    operator fun <C : Capability<*, *>, T : Any> invoke(
        source: Capability.Optional<C>,
        mapper: Mapper<C, T>,
    ): T? = when (source) {
        is Capability.Available -> mapper(source.value)
        Capability.Unsupported -> null
    }
}

object CapabilityConstraintDomainMapper {
    fun <C : Capability<*, *>, T : Any> required(
        source: T,
        mapper: Mapper<T, C>,
    ): Capability.Required<C> = Capability.Required(value = mapper(source))

    fun <C : Capability<*, *>, T : Any> optional(
        source: T?,
        mapper: Mapper<T, C>,
    ): Capability.Optional<C> = when (source) {
        null -> Capability.Unsupported
        else -> Capability.Available(value = mapper(source))
    }
}
