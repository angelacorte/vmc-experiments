package it.unibo.collektive.alchemist.device.sensors

interface RandomGenerator {
    fun nextRandomDouble(): Double

    fun nextRandomDouble(until: Double): Double

    fun nextRandomDouble(range: ClosedFloatingPointRange<Double>): Double

    fun nextGaussian(): Double
}
