package it.unibo.collektive.alchemist.device.sensors

interface ResourceSensor {
    val resourceLowerBound: Double

    fun getResource(): Double

    fun setCurrentOverallResource(resource: Double)
}
