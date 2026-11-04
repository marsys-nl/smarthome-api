package network.marsys.smarthome.api.models.zone

import kotlinx.serialization.Serializable

@Serializable
data class ZoneResponse(
    val identifier: String,
    val naming: Naming,
) {
    @Serializable
    data class Naming(
        val key: String,
        val qualifier: String? = null,
    )
}
