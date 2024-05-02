package it.unibo.collektive.alchemist.device.sensors

interface ResourceSensor {
    fun getResource(): Double

    fun setCurrentOverallResource(resource: Double)
}
