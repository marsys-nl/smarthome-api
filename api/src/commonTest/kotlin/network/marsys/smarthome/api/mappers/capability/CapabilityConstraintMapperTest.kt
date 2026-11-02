package network.marsys.smarthome.api.mappers.capability

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isNull
import network.marsys.smarthome.domain.capability.Brightness
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.capability.Capability.Companion.optional
import network.marsys.smarthome.domain.capability.Capability.Companion.required
import network.marsys.smarthome.domain.capability.OnOff
import network.marsys.smarthome.domain.unit.percent

val CapabilityConstraintMapperTest by testSuite(
    name = "Capability constraint mapper tests",
) {
    testSuite(
        name = "Map capability constraints to API model",
    ) {
        test(name = "Mapping succeeds with required capability") {
            val source: Capability.Required<OnOff> = required(OnOff(current = true))

            expectThat(CapabilityConstraintApiMapper.invoke(source, mapper = OnOffApiMapper))
                .isEqualTo(true)
        }

        test(name = "Mapping succeeds with available optional capability") {
            val source: Capability.Optional<Brightness> = optional(Brightness(current = 50.percent))

            expectThat(CapabilityConstraintApiMapper.invoke(source, mapper = BrightnessApiMapper))
                .isEqualTo(50.0)
        }

        test(name = "Mapping succeeds with missing optional capability") {
            val source: Capability.Optional<Brightness> = optional(null)

            expectThat(CapabilityConstraintApiMapper.invoke(source, mapper = BrightnessApiMapper))
                .isNull()
        }
    }

    testSuite(
        name = "Map API model to capability constraints",
    ) {
        test(name = "Mapping succeeds when capability is required") {
            val source = true

            expectThat(CapabilityConstraintDomainMapper.required(source, mapper = OnOffDomainMapper))
                .isA<Capability.Required<*>>()
                .get(Capability.Required<*>::value)
                .isA<OnOff>()
                .get(OnOff::current)
                .isEqualTo(source)
        }

        test(name = "Mapping succeeds when capability is optional and present") {
            val source = 50.0

            expectThat(CapabilityConstraintDomainMapper.optional(source, mapper = BrightnessDomainMapper))
                .isA<Capability.Available<*>>()
                .get(Capability.Available<*>::value)
                .isA<Brightness>()
                .get(Brightness::current)
                .isEqualTo(source.percent)
        }

        test(name = "Mapping succeeds when capability is optional and missing") {
            val source = null

            expectThat(CapabilityConstraintDomainMapper.optional(source, mapper = BrightnessDomainMapper))
                .isA<Capability.Unsupported>()
        }
    }
}
