package network.marsys.smarthome.api.models.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("light")
data class LightEntity(
    override val identifier: String,
    override val state: State? = null,
) : EntityResponse {
    @Serializable
    data class State(
        val onOff: Boolean,
        val brightness: Float? = null,
    ) : EntityResponse.State
}
