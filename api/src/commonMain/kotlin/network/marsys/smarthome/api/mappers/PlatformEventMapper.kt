package network.marsys.smarthome.api.mappers

import network.marsys.smarthome.api.mappers.platform.CapabilityApiMapper
import network.marsys.smarthome.api.mappers.platform.CapabilityDomainMapper
import network.marsys.smarthome.api.models.platform.PlatformEventMessage
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.platform.PlatformEvent
import kotlin.time.Instant

object PlatformEventApiMapper : Mapper<PlatformEvent, PlatformEventMessage> {
    override fun invoke(source: PlatformEvent): PlatformEventMessage =
        when (source) {
            is PlatformEvent.CapabilityUpdated -> CapabilityUpdatedApiMapper.invoke(source)
        }
}

object PlatformEventDomainMapper : Mapper<PlatformEventMessage, PlatformEvent> {
    override fun invoke(source: PlatformEventMessage): PlatformEvent =
        when (source) {
            is PlatformEventMessage.CapabilityUpdated -> CapabilityUpdatedDomainMapper.invoke(source)
        }
}

private object CapabilityUpdatedApiMapper :
    Mapper<PlatformEvent.CapabilityUpdated, PlatformEventMessage.CapabilityUpdated> {
    override fun invoke(source: PlatformEvent.CapabilityUpdated): PlatformEventMessage.CapabilityUpdated =
        PlatformEventMessage.CapabilityUpdated(
            identifier = source.identifier.value,
            capability = CapabilityApiMapper.invoke(source = source.capability),
            occurredAt = source.occurredAt.toString(),
        )
}

private object CapabilityUpdatedDomainMapper :
    Mapper<PlatformEventMessage.CapabilityUpdated, PlatformEvent.CapabilityUpdated> {
    override fun invoke(source: PlatformEventMessage.CapabilityUpdated): PlatformEvent.CapabilityUpdated =
        PlatformEvent.CapabilityUpdated(
            identifier = EntityIdentifier(source.identifier),
            capability = CapabilityDomainMapper.invoke(source = source.capability),
            occurredAt = Instant.parse(source.occurredAt),
        )
}
