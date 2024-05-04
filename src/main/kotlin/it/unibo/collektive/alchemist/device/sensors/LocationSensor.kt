package it.unibo.collektive.alchemist.device.sensors

interface LocationSensor {
    val evaluationRadius: Double

    fun coordinates(): Pair<Double, Double>

    fun displacements(): List<Pair<Double, Double>>
}
