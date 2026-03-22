package network.marsys.smarthome.api.models.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationResponse(
    val baseUri: String,
)
