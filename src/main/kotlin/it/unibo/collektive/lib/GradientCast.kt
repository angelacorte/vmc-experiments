package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.aggregate.api.operators.share
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.field.min
import it.unibo.collektive.field.plus
import kotlin.Double.Companion.POSITIVE_INFINITY

context(DistanceSensor)
fun <ID: Any> Aggregate<ID>.gradientCast(source: Boolean, initial: Double): Double =
    share(initial) { field ->
        val dist = distances()
        when (source) {
            true -> 0.0
            else -> {
                val min = (field + dist).min(POSITIVE_INFINITY)
                if (min == POSITIVE_INFINITY) {
                    initial
                } else {
                    min
                }
            }
        }
    }

context(DistanceSensor)
fun <ID: Any> Aggregate<ID>.distanceTo(source: Boolean): Double =
    gradientCast(source, if (source) 0.0 else POSITIVE_INFINITY)
