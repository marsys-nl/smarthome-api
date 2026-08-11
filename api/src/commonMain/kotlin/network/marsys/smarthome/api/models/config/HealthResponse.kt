package network.marsys.smarthome.api.models.config

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    @Serializable
    val app: String,
    @Serializable
    val version: String,
)
