package network.marsys.smarthome.api.models.platform

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface PlatformEventMessage {
    @Serializable
    @SerialName("capability-updated")
    data class CapabilityUpdated(
        val identifier: String,
        val capability: CapabilityMessage,
        val occurredAt: String,
    ) : PlatformEventMessage
}
