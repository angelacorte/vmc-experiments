package it.unibo.collektive.alchemist.device.sensors

interface LocationSensor {
    fun coordinates(): Pair<Double, Double>

    fun surroundings(): List<Pair<Double, Double>>
}
