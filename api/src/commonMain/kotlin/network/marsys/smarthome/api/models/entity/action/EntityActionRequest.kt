package network.marsys.smarthome.api.models.entity.action

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed interface EntityActionRequest {
    val entity: String

    @Serializable
    @SerialName("movement.close")
    data class Close(
        override val entity: String,
    ) : EntityActionRequest

    @Serializable
    @SerialName("movement.open")
    data class Open(
        override val entity: String,
    ) : EntityActionRequest

    @Serializable
    @SerialName("movement.stop")
    data class Stop(
        override val entity: String,
    ) : EntityActionRequest

    @Serializable
    @SerialName("brightness.set")
    data class SetBrightness(
        override val entity: String,
        val brightness: Double,
    ) : EntityActionRequest

    @Serializable
    @SerialName("target-temperature.set")
    data class SetTargetTemperature(
        override val entity: String,
        val temperature: Double,
    ) : EntityActionRequest

    @Serializable
    @SerialName("on-off.on")
    data class TurnOn(
        override val entity: String,
    ) : EntityActionRequest

    @Serializable
    @SerialName("on-off.off")
    data class TurnOff(
        override val entity: String,
    ) : EntityActionRequest
}
