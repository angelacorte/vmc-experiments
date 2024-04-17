package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.field.Field.Companion.hood
import kotlin.math.max

context(Aggregate<Int>, DistanceSensor, EnvironmentVariables)
fun convergeAndSpread(): Double {
    val potential = distanceTo(localId == 0)
    set("potential", potential)
    val localSuccess = max(0, 20 - neighboring(1).hood(0) { a, b -> a + b})
    set("localSuccess", localSuccess)
    val success = convergeCast(potential, localSuccess) { a, b -> a + b }
    set("success", success)
    return spreadToChildren(potential, if (localId == 0) 200.0 else 0.0, success.toDouble()).also {
        set("resource", it)
    }
}
