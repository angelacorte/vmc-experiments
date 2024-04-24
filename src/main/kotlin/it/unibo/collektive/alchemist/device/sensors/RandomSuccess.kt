package it.unibo.collektive.alchemist.device.sensors

interface RandomSuccess {
    fun nextRandomDouble(): Double

    fun nextRandomDouble(until: Double): Double

    fun nextRandomDouble(range: ClosedFloatingPointRange<Double>): Double
}