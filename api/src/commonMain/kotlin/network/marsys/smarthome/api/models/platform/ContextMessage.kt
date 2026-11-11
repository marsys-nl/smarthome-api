package network.marsys.smarthome.api.models.platform

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject

@Serializable
data class ContextMessage(
    @Suppress("ktlint:standard:annotation")
    val elements: List<@Serializable(with = ContextElementMessageSerializer::class) ContextElementMessage> =
        emptyList(),
)

internal object ContextElementMessageSerializer : JsonTransformingSerializer<ContextElementMessage>(
    tSerializer = ContextElementMessage.serializer(),
) {
    override fun transformSerialize(element: JsonElement): JsonElement =
        element.jsonObject.getValue("type")

    override fun transformDeserialize(element: JsonElement): JsonElement =
        when (element) {
            is JsonPrimitive if element.isString ->
                JsonObject(mapOf("type" to element))

            else ->
                throw SerializationException("Unsupported type: $element")
        }
}
