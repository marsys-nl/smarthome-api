package network.marsys.smarthome.api.models.integration

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.marsys.smarthome.domain.identifiers.IntegrationIdentifier

@Serializable
data class IntegrationResponse(
    val identifier: IntegrationIdentifier,
    val status: Status,
) {
    @Serializable
    sealed interface Status {
        @Serializable
        @SerialName("Starting")
        data object Starting : Status

        @Serializable
        @SerialName("Running")
        data object Running : Status

        @Serializable
        @SerialName("Degraded")
        data object Degraded : Status

        @Serializable
        @SerialName("Stopping")
        data object Stopping : Status

        @Serializable
        @SerialName("Stopped")
        data object Stopped : Status

        @Serializable
        @SerialName("Failed")
        data object Failed : Status
    }
}
