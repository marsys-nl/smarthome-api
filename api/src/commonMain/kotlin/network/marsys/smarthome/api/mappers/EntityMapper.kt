package network.marsys.smarthome.api.mappers

import network.marsys.smarthome.api.mappers.entity.BlindApiMapper
import network.marsys.smarthome.api.mappers.entity.BlindDomainMapper
import network.marsys.smarthome.api.mappers.entity.CameraApiMapper
import network.marsys.smarthome.api.mappers.entity.CameraDomainMapper
import network.marsys.smarthome.api.mappers.entity.CurtainApiMapper
import network.marsys.smarthome.api.mappers.entity.CurtainDomainMapper
import network.marsys.smarthome.api.mappers.entity.FanApiMapper
import network.marsys.smarthome.api.mappers.entity.FanDomainMapper
import network.marsys.smarthome.api.mappers.entity.LightApiMapper
import network.marsys.smarthome.api.mappers.entity.LightDomainMapper
import network.marsys.smarthome.api.mappers.entity.ShutterApiMapper
import network.marsys.smarthome.api.mappers.entity.ShutterDomainMapper
import network.marsys.smarthome.api.mappers.entity.SmartLockApiMapper
import network.marsys.smarthome.api.mappers.entity.SmartLockDomainMapper
import network.marsys.smarthome.api.mappers.entity.SmartPlugApiMapper
import network.marsys.smarthome.api.mappers.entity.SmartPlugDomainMapper
import network.marsys.smarthome.api.mappers.entity.SpeakerApiMapper
import network.marsys.smarthome.api.mappers.entity.SpeakerDomainMapper
import network.marsys.smarthome.api.mappers.entity.SystemApiMapper
import network.marsys.smarthome.api.mappers.entity.SystemDomainMapper
import network.marsys.smarthome.api.mappers.entity.ThermostatApiMapper
import network.marsys.smarthome.api.mappers.entity.ThermostatDomainMapper
import network.marsys.smarthome.api.models.entity.BlindEntityResponse
import network.marsys.smarthome.api.models.entity.CameraEntityResponse
import network.marsys.smarthome.api.models.entity.CurtainEntityResponse
import network.marsys.smarthome.api.models.entity.EntityResponse
import network.marsys.smarthome.api.models.entity.FanEntityResponse
import network.marsys.smarthome.api.models.entity.LightEntityResponse
import network.marsys.smarthome.api.models.entity.ShutterEntityResponse
import network.marsys.smarthome.api.models.entity.SmartLockEntityResponse
import network.marsys.smarthome.api.models.entity.SmartPlugEntityResponse
import network.marsys.smarthome.api.models.entity.SpeakerEntityResponse
import network.marsys.smarthome.api.models.entity.SystemEntityResponse
import network.marsys.smarthome.api.models.entity.ThermostatEntityResponse
import network.marsys.smarthome.domain.entity.Blind
import network.marsys.smarthome.domain.entity.Camera
import network.marsys.smarthome.domain.entity.Curtain
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.Fan
import network.marsys.smarthome.domain.entity.Light
import network.marsys.smarthome.domain.entity.Shutter
import network.marsys.smarthome.domain.entity.SmartLock
import network.marsys.smarthome.domain.entity.SmartPlug
import network.marsys.smarthome.domain.entity.Speaker
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.domain.entity.Thermostat

object EntityApiMapper : Mapper<Entity, EntityResponse> {
    override fun invoke(source: Entity) = when (source) {
        is Blind -> BlindApiMapper.invoke(source = source)
        is Camera -> CameraApiMapper.invoke(source = source)
        is Curtain -> CurtainApiMapper.invoke(source = source)
        is Fan -> FanApiMapper.invoke(source = source)
        is Light -> LightApiMapper.invoke(source = source)
        is Shutter -> ShutterApiMapper.invoke(source = source)
        is SmartLock -> SmartLockApiMapper.invoke(source = source)
        is SmartPlug -> SmartPlugApiMapper.invoke(source = source)
        is Speaker -> SpeakerApiMapper.invoke(source = source)
        is System -> SystemApiMapper.invoke(source = source)
        is Thermostat -> ThermostatApiMapper.invoke(source = source)
    }
}

object EntityCollectionApiMapper : Mapper<Collection<Entity>, Collection<EntityResponse>> {
    override fun invoke(source: Collection<Entity>) = source.map(EntityApiMapper::invoke)
}

object EntityDomainMapper : Mapper<EntityResponse, Entity> {
    override fun invoke(source: EntityResponse) = when (source) {
        is BlindEntityResponse -> BlindDomainMapper.invoke(source = source)
        is CameraEntityResponse -> CameraDomainMapper.invoke(source = source)
        is CurtainEntityResponse -> CurtainDomainMapper.invoke(source = source)
        is FanEntityResponse -> FanDomainMapper.invoke(source = source)
        is LightEntityResponse -> LightDomainMapper.invoke(source = source)
        is ShutterEntityResponse -> ShutterDomainMapper.invoke(source = source)
        is SmartLockEntityResponse -> SmartLockDomainMapper.invoke(source = source)
        is SmartPlugEntityResponse -> SmartPlugDomainMapper.invoke(source = source)
        is SpeakerEntityResponse -> SpeakerDomainMapper.invoke(source = source)
        is SystemEntityResponse -> SystemDomainMapper.invoke(source = source)
        is ThermostatEntityResponse -> ThermostatDomainMapper.invoke(source = source)
    }
}

object EntityCollectionDomainMapper : Mapper<Collection<EntityResponse>, Collection<Entity>> {
    override fun invoke(source: Collection<EntityResponse>) = source.map(EntityDomainMapper::invoke)
}
