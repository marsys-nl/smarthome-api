package network.marsys.smarthome.api.models.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.marsys.smarthome.domain.identifiers.EntityIdentifier

@Serializable
@SerialName("light")
data class LightEntity(
    override val identifier: EntityIdentifier,
    override val naming: EntityResponse.Naming,
    override val state: State? = null,
) : EntityResponse {
    @Serializable
    data class State(
        val onOff: Boolean,
        val brightness: Double? = null,
    ) : EntityResponse.State
}
