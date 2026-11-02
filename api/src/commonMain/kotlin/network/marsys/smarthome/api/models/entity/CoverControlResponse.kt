package network.marsys.smarthome.api.models.entity

import kotlinx.serialization.Serializable

@Serializable
data class CoverControlResponse(
    val movement: String? = null,
    val opened: Boolean? = null,
    val position: Double? = null,
)
