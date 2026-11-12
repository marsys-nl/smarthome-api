package network.marsys.smarthome.api.mappers.platform

import de.infix.testBalloon.framework.core.testSuite
import dev.nmarsman.expect.api.expectThat
import dev.nmarsman.expect.api.expectThrows
import dev.nmarsman.expect.assertions.containsExactly
import dev.nmarsman.expect.assertions.hasMessage
import dev.nmarsman.expect.assertions.hasSize
import dev.nmarsman.expect.assertions.isA
import dev.nmarsman.expect.assertions.isEmpty
import dev.nmarsman.expect.assertions.isEqualTo
import dev.nmarsman.expect.assertions.isTrue
import dev.nmarsman.expect.assertions.single
import network.marsys.smarthome.api.models.platform.ContextElementMessage
import network.marsys.smarthome.api.models.platform.ContextMessage
import network.marsys.smarthome.domain.capability.Capability
import network.marsys.smarthome.domain.entity.System
import network.marsys.smarthome.domain.entity.Thermostat
import network.marsys.smarthome.domain.entity.formattedName

val ContextMapperTest by testSuite(
    name = "Context mapper tests",
) {
    val examples = mapOf(
        System.MemoryType.Available to ContextElementMessage.MemoryType.Available,
        System.MemoryType.Total to ContextElementMessage.MemoryType.Total,
        System.MemoryType.SwapAvailable to ContextElementMessage.MemoryType.SwapAvailable,
        System.MemoryType.SwapTotal to ContextElementMessage.MemoryType.SwapTotal,
        Thermostat.TemperatureType.Current to ContextElementMessage.TemperatureType.Current,
        Thermostat.TemperatureType.Outdoor to ContextElementMessage.TemperatureType.Outdoor,
    )

    test(name = "Mapping an empty context results in an empty context message") {
        val context = Capability.Context.Empty

        expectThat(ContextApiMapper.invoke(context))
            .isA<ContextMessage>()
            .get(ContextMessage::elements)
            .isEmpty()
    }

    examples.forEach { (original, expected) ->
        test(
            name = "Mapping context element '${original::class.formattedName}' results in " +
                "a context element message of type ${expected::class.formattedName}",
        ) {
            val context = Capability.Context.Empty + original

            expectThat(ContextApiMapper.invoke(context))
                .isA<ContextMessage>()
                .get(ContextMessage::elements)
                .single()
                .isEqualTo(expected)
        }
    }

    test(name = "Mapping multiple context elements results in a context message that contains all context elements - different types") {
        val context = Capability.Context.Empty + System.MemoryType.Total + Thermostat.TemperatureType.Outdoor

        expectThat(ContextApiMapper.invoke(context))
            .isA<ContextMessage>()
            .get(ContextMessage::elements)
            .hasSize(2)
            .containsExactly(
                ContextElementMessage.MemoryType.Total,
                ContextElementMessage.TemperatureType.Outdoor,
            )
    }

    test(name = "Mapping multiple context elements results in a context message that contains all context elements - same type") {
        val context = Capability.Context.Empty + System.MemoryType.Total + System.MemoryType.Available

        expectThat(ContextApiMapper.invoke(context))
            .isA<ContextMessage>()
            .get(ContextMessage::elements)
            .hasSize(2)
            .containsExactly(
                ContextElementMessage.MemoryType.Total,
                ContextElementMessage.MemoryType.Available,
            )
    }

    test(name = "Mapping an unsupported context element results in an error") {
        val context = Capability.Context.Empty + Unsupported

        expectThrows<IllegalStateException> { ContextApiMapper.invoke(context) }
            .hasMessage("Unsupported context element type: Unsupported")
    }

    test(name = "Mapping an empty context message results in an empty context") {
        val message = ContextMessage(elements = emptyList())

        expectThat(ContextDomainMapper.invoke(message))
            .isA<Capability.Context.Empty>()
    }

    examples.forEach { (expected, original) ->
        test(
            name = "Mapping context element message of type '${original::class.formattedName}' " +
                "results in a context element of type ${expected::class.formattedName}",
        ) {
            val message = ContextMessage(elements = listOf(original))

            expectThat(ContextDomainMapper.invoke(message))
                .isA<Capability.Context>()
                .get { expected in this }
                .isTrue()
        }
    }

    test(name = "Mapping multiple context element messages results in a context that contains all context elements - different types") {
        val message = ContextMessage(
            elements = listOf(
                ContextElementMessage.MemoryType.Total,
                ContextElementMessage.TemperatureType.Outdoor,
            ),
        )

        expectThat(ContextDomainMapper.invoke(message))
            .isA<Capability.Context>()
            .get { System.MemoryType.Total in this && Thermostat.TemperatureType.Outdoor in this }
            .isTrue()
    }

    test(name = "Mapping multiple context element messages results in a context that contains all context elements - same type") {
        val message = ContextMessage(
            elements = listOf(
                ContextElementMessage.MemoryType.Total,
                ContextElementMessage.MemoryType.Available,
            ),
        )

        expectThat(ContextDomainMapper.invoke(message))
            .isA<Capability.Context>()
            .get { System.MemoryType.Total in this && System.MemoryType.Available in this }
            .isTrue()
    }
}

private data object Unsupported : Capability.Context.Element, Capability.Context.Key<Unsupported> {
    override val key: Capability.Context.Key<Unsupported> = Unsupported
}
