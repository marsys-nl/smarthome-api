package network.marsys.smarthome.api.models.entity

import kotlinx.serialization.Serializable

@Serializable
sealed interface EntityResponse {
    val identifier: String
    val naming: Naming
    val state: State?

    @Serializable
    data class Naming(
        val key: String,
        val qualifier: String? = null,
    )

    @Serializable
    sealed interface State
}
