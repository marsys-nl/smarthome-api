package network.marsys.smarthome.api

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import network.marsys.smarthome.api.models.integration.IntegrationResponse

val apiModuleSerializersModule = SerializersModule {
    polymorphic(IntegrationResponse.Status::class) {
        subclass(IntegrationResponse.Status.Starting::class)
        subclass(IntegrationResponse.Status.Running::class)
        subclass(IntegrationResponse.Status.Degraded::class)
        subclass(IntegrationResponse.Status.Stopping::class)
        subclass(IntegrationResponse.Status.Stopped::class)
    }
}
