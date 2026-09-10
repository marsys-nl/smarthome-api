package network.marsys.smarthome.api.models.entity

import kotlinx.serialization.Serializable

@Serializable
sealed interface EntityResponse {
    val identifier: String
    val state: State?

    @Serializable
    sealed interface State
}
