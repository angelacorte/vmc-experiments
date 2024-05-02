package it.unibo.collektive.alchemist.device.sensors

interface LocationSensor {
    fun coordinates(): Pair<Double, Double>
}
