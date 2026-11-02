package network.marsys.smarthome.api.mappers

import network.marsys.smarthome.domain.entity.EntityIdentifier

object EntityIdentifierApiMapper : Mapper<EntityIdentifier, String> {
    override fun invoke(source: EntityIdentifier): String =
        source.value
}

object EntityIdentifierDomainMapper : Mapper<String, EntityIdentifier> {
    override fun invoke(source: String): EntityIdentifier =
        EntityIdentifier(source)
}
