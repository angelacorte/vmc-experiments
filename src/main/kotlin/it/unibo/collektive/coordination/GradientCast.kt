package it.unibo.collektive.coordination

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.aggregate.api.operators.share
import it.unibo.collektive.field.Field.Companion.fold
import it.unibo.collektive.field.min
import it.unibo.collektive.field.plus
import kotlin.Double.Companion.POSITIVE_INFINITY

context(DistanceSensor)
fun <ID : Any> Aggregate<ID>.gradientCast(source: Boolean, initial: Double): Double =
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
fun <ID : Any> Aggregate<ID>.distanceTo(source: Boolean): Double =
    gradientCast(source, if (source) 0.0 else POSITIVE_INFINITY)

context(DistanceSensor)
fun <ID : Any> Aggregate<ID>.distanceBetween(source: Boolean, destination: Boolean): Double =
    broadcast(source, distanceTo(destination)) { it }

context(DistanceSensor)
fun <ID : Any> Aggregate<ID>.broadcast(source: Boolean, value: Double, accumulator: (Double) -> Double): Double =
    share(POSITIVE_INFINITY to value) { field ->
        val dist = distances()
        when {
            source -> 0.0 to value
            else -> {
                val resultField = dist.alignedMap(field) { distField, (currDist, value) ->
                    distField + currDist to accumulator(value)
                }
                resultField.fold(POSITIVE_INFINITY to POSITIVE_INFINITY) { acc, value ->
                    if (value.first < acc.first) value else acc
                }
            }
        }
    }.second
