package network.marsys.smarthome.api.models.platform

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull

@Serializable(with = CapabilityMessageSerializer::class)
sealed interface CapabilityMessage {
    val type: String
    val context: ContextMessage

    @Serializable
    data class BooleanValue(
        override val type: String,
        override val context: ContextMessage,
        val current: Boolean,
    ) : CapabilityMessage

    @Serializable
    data class DoubleValue(
        override val type: String,
        override val context: ContextMessage,
        val current: Double,
    ) : CapabilityMessage

    @Serializable
    data class EnumValue(
        override val type: String,
        override val context: ContextMessage,
        val current: String,
    ) : CapabilityMessage
}

internal object CapabilityMessageSerializer : JsonContentPolymorphicSerializer<CapabilityMessage>(
    baseClass = CapabilityMessage::class,
) {
    override fun selectDeserializer(
        element: JsonElement,
    ): DeserializationStrategy<CapabilityMessage> {
        val current = (element as? JsonObject)
            ?.get("current") as? JsonPrimitive
            ?: throw SerializationException("Missing or invalid capability current value")

        return when {
            current.isString ->
                CapabilityMessage.EnumValue.serializer()

            current.booleanOrNull != null ->
                CapabilityMessage.BooleanValue.serializer()

            current.doubleOrNull != null ->
                CapabilityMessage.DoubleValue.serializer()

            else ->
                throw SerializationException("Unsupported capability value: $current")
        }
    }
}
