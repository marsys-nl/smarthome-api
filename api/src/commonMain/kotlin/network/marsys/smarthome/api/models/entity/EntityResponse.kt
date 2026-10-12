package network.marsys.smarthome.api.models.entity

import kotlinx.serialization.Serializable
import network.marsys.smarthome.domain.identifiers.EntityIdentifier

@Serializable
sealed interface EntityResponse {
    val identifier: EntityIdentifier
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
