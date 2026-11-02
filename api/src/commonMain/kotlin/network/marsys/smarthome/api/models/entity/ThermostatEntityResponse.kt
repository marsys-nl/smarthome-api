package network.marsys.smarthome.api.models.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import network.marsys.smarthome.domain.capability.WindowDetection

@Serializable
@SerialName("thermostat")
data class ThermostatEntityResponse(
    override val identifier: String,
    override val naming: EntityResponse.Naming,
    override val state: State? = null,
) : EntityResponse {
    @Serializable
    data class State(
        val onOff: Boolean,
        val mode: String,
        val temperatures: Temperatures,
        @SerialName("child-lock")
        val childLock: Boolean?,
        @SerialName("window-detection")
        val windowDetection: Boolean?,
        @SerialName("scheduled-mode")
        val scheduledMode: Boolean?,
    ) : EntityResponse.State

    @Serializable
    data class Temperatures(
        val target: Double,
        val current: Double,
        val outdoor: Double?,
    )
}
