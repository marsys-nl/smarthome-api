package network.marsys.smarthome.api.models.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
@SerialName("system")
data class SystemEntity(
    override val identifier: String,
    override val state: State? = null,
) : EntityResponse {
    @Serializable
    data class State(
        val host: Host,
        val processor: Processor,
        val memory: Memory,
        val uptime: Uptime,
    ) : EntityResponse.State

    @Serializable
    data class Host(
        val device: Device,
        @SerialName("os")
        val operatingSystem: OperatingSystem,
    )

    @Serializable
    data class Device(
        val manufacturer: String,
        val model: String,
        val architecture: String,
        val physicalCores: Int,
        val logicalCores: Int,
    )

    @Serializable
    data class OperatingSystem(
        val description: String,
        val family: String,
        val version: String? = null,
        val bitness: Int,
    )

    @Serializable
    data class Processor(
        val load: Float,
        val temperature: Float? = null,
    )

    @Serializable
    data class Memory(
        val total: Float,
        val available: Float,
        val swap: Swap,
    )

    @Serializable
    data class Swap(
        val total: Float,
        val used: Float,
    )

    @Serializable
    data class Uptime(
        val host: Instant,
        val application: Instant,
    )
}
