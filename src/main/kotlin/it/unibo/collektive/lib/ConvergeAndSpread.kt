package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.CollektiveDevice
import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.RandomGenerator
import it.unibo.collektive.field.Field

context(RandomGenerator, CollektiveDevice<*>)
fun Aggregate<Int>.convergeAndSpread(): Double {
    val (leader, potential) = with(MyHopMetric()) {
        val l = boundedElection(localId, 12.0) //localId == 0 //
        l to distanceTo(l)
    }
    set("source", leader)
    set("potential", potential)
    val localSuccess = nextRandomDouble(0.0..20.0)
//    val count = repeat(1.0) { a -> a + 1.0}
//    val localSuccess = if (count < 400.0) nextRandomDouble(0.0..20.0) else get("localSuccess")
    set("localSuccess", localSuccess)
    val success = convergeCast(potential, localSuccess) { a, b -> a + b }
    set("success", success)
    return spreadToChildren(potential, if (leader) 500.0 else 0.0, success).also {
        set("resource", it)
        if (it >= 200) {
            val cloned = repeat(1) { a -> a + 1 }
            if(cloned % 500 == 0) {
                set("cloning", true)
            }
        }
    }
}

data class MyHopMetric(val step: Double = 1.0) : DistanceSensor {
    override fun <ID : Any> Aggregate<ID>.distances(): Field<ID, Double> =
        neighboring(step)
            .mapWithId { id, value ->
            if (id == localId) 0.0 else value + step
        }
}
