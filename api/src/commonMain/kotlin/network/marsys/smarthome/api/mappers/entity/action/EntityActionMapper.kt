package network.marsys.smarthome.api.mappers.entity.action

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.api.models.entity.action.EntityActionRequest
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Movement
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.capability.TargetTemperature
import network.marsys.smarthome.domain.entity.Entity
import network.marsys.smarthome.domain.entity.EntityIdentifier
import network.marsys.smarthome.domain.unit.celsius
import network.marsys.smarthome.domain.unit.percent

object EntityActionApiMapper : Mapper<Entity.Action, EntityActionRequest?> {
    override fun invoke(source: Entity.Action): EntityActionRequest? =
        when (source) {
            is Brightness.SetBrightness ->
                EntityActionRequest.SetBrightness(
                    entity = source.entity.value,
                    brightness = source.brightness.value,
                )

            is Movement.Move.Open ->
                EntityActionRequest.Open(
                    entity = source.entity.value,
                )

            is Movement.Move.Close ->
                EntityActionRequest.Close(
                    entity = source.entity.value,
                )

            is Movement.Move.Stop ->
                EntityActionRequest.Stop(
                    entity = source.entity.value,
                )

            is OnOff.Toggle.On ->
                EntityActionRequest.TurnOn(
                    entity = source.entity.value,
                )

            is OnOff.Toggle.Off ->
                EntityActionRequest.TurnOff(
                    entity = source.entity.value,
                )

            is TargetTemperature.SetTargetTemperature ->
                EntityActionRequest.SetTargetTemperature(
                    entity = source.entity.value,
                    temperature = source.temperature.value,
                )

            else -> null
        }
}

object EntityActionDomainMapper : Mapper<EntityActionRequest, Entity.Action> {
    override fun invoke(source: EntityActionRequest): Entity.Action =
        when (source) {
            is EntityActionRequest.SetBrightness ->
                Brightness.SetBrightness(
                    entity = EntityIdentifier(source.entity),
                    brightness = source.brightness.percent,
                )

            is EntityActionRequest.Open ->
                Movement.Move.Open(
                    entity = EntityIdentifier(source.entity),
                )

            is EntityActionRequest.Close ->
                Movement.Move.Close(
                    entity = EntityIdentifier(source.entity),
                )

            is EntityActionRequest.Stop ->
                Movement.Move.Stop(
                    entity = EntityIdentifier(source.entity),
                )

            is EntityActionRequest.TurnOn ->
                OnOff.Toggle.On(
                    entity = EntityIdentifier(source.entity),
                )

            is EntityActionRequest.TurnOff ->
                OnOff.Toggle.Off(
                    entity = EntityIdentifier(source.entity),
                )

            is EntityActionRequest.SetTargetTemperature ->
                TargetTemperature.SetTargetTemperature(
                    entity = EntityIdentifier(source.entity),
                    temperature = source.temperature.celsius,
                )
        }
}
