package network.marsys.smarthome.api.models.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("smart-lock")
data class SmartLockEntityResponse(
    override val identifier: String,
    override val naming: EntityResponse.Naming,
    override val state: State? = null,
) : EntityResponse {
    @Serializable
    data class State(
        val onOff: Boolean,
    ) : EntityResponse.State
}
