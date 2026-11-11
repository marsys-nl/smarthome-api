package network.marsys.smarthome.api.models.platform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface ContextElementMessage {
    @Serializable
    sealed interface MemoryType : ContextElementMessage {
        @Serializable
        @SerialName("total-memory-type")
        data object Total : MemoryType

        @Serializable
        @SerialName("available-memory-type")
        data object Available : MemoryType

        @Serializable
        @SerialName("swap-total-memory-type")
        data object SwapTotal : MemoryType

        @Serializable
        @SerialName("swap-available-memory-type")
        data object SwapAvailable : MemoryType
    }

    @Serializable
    sealed interface TemperatureType : ContextElementMessage {
        @Serializable
        @SerialName("current-temperature-type")
        data object Current : TemperatureType

        @Serializable
        @SerialName("outdoor-temperature-type")
        data object Outdoor : TemperatureType
    }
}
