package network.marsys.smarthome.api.models.integration

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
        data object Starting : Status

        @Serializable
        data object Running : Status

        @Serializable
        data object Degraded : Status

        @Serializable
        data object Stopping : Status

        @Serializable
        data object Stopped : Status
    }
}
