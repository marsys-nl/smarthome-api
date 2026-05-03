package network.marsys.smarthome.api.models.config

import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    @Serializable
    val app: String = "network.marsys.smarthome",
    @Serializable
    val version: String = "2026.05",
)
