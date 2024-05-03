package it.unibo.collektive.alchemist.device.sensors

interface ResourceSensor {
    val leaderInitialResource: Double

    fun getResource(): Double

    fun setInitialResource(source: Boolean)

    fun updateResource(resource: Double)
}
