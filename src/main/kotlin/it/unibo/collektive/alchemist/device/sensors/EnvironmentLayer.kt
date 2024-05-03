package it.unibo.collektive.alchemist.device.sensors

interface EnvironmentLayer {
    fun <T> getFromLayer(name: String): T

    fun <T> getFromLayerOrNull(name: String): T?

    fun <T> getFromLayerOrDefault(name: String, default: T): T

    fun isLayerDefined(name: String): Boolean
}
