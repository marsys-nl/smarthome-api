package network.marsys.smarthome.api.mappers

fun interface Mapper<S, T> {
    operator fun invoke(source: S): T
}
