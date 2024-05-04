package it.unibo.collektive.alchemist.device.sensors

interface ResourceSensor {
    val leaderInitialResource: Double

    val defaultResource: Double
        get() = 0.0

    fun getResource(): Double

    fun setInitialResource(source: Boolean)

    fun updateResource(resource: Double)
}
