package network.marsys.smarthome.api.mappers

import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.domain.entity.EntityNaming
import network.marsys.smarthome.domain.i18n.TranslationKey

object EntityNamingApiMapper : Mapper<EntityNaming, EntityResponse.Naming> {
    override fun invoke(source: EntityNaming): EntityResponse.Naming =
        EntityResponse.Naming(
            key = source.key.value,
            qualifier = source.qualifier?.value,
        )
}

object EntityNamingDomainMapper : Mapper<EntityResponse.Naming, EntityNaming> {
    override fun invoke(source: EntityResponse.Naming): EntityNaming =
        EntityNaming(
            key = TranslationKey(source.key),
            qualifier = source.qualifier?.let { TranslationKey(it) },
        )
}
