package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentLayer
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.coordination.convergeCast

context(DistanceSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.convergeSuccess(potential: Double, localSuccess: Double): Double {
    val success = convergeCast(potential, localSuccess) { a, b -> a + b }
    return set("success", success)
}

context(DistanceSensor, EnvironmentVariables, EnvironmentLayer)
fun <ID : Comparable<ID>> Aggregate<ID>.obtainLocalSuccess(): Double {
    val localSuccess = getFromLayer<Double>("successSource")
    return set("localSuccess", localSuccess)
}

context(DistanceSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.applyNegativeForce(localSuccess: Double): Double {
    val negativeForce = getOrDefault("negativeForce", 0.5)
    return localSuccess - (localSuccess * negativeForce)
}