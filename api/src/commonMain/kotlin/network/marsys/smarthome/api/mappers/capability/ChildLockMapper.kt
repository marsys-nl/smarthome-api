package network.marsys.smarthome.api.mappers.capability

import network.marsys.smarthome.api.mappers.Mapper
import network.marsys.smarthome.domain.capability.ChildLock

object ChildLockApiMapper : Mapper<ChildLock, Boolean> {
    override fun invoke(source: ChildLock) = source.current
}

object ChildLockDomainMapper : Mapper<Boolean, ChildLock> {
    override fun invoke(source: Boolean) = ChildLock(current = source)
}
