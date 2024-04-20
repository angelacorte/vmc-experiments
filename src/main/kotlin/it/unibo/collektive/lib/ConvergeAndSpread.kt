package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.RandomGenerator
import it.unibo.collektive.field.Field

context(DistanceSensor, EnvironmentVariables, RandomGenerator)
fun Aggregate<Int>.convergeAndSpread(): Double {
    val leader = boundedElection(localId, 15.0) //localId == 0 //
    set("source", leader)
    val potential = distanceTo(leader)
    set("potential", potential)
//    val localSuccess = max(0, 20 - neighboring(1).hood(0) { a, b -> a + b})
    val localSuccess = nextRandomDouble(0.0..20.0)
    set("localSuccess", localSuccess)
    val success = convergeCast(potential, localSuccess) { a, b -> a + b }
    set("success", success)
    return spreadToChildren(potential, if (leader) 500.0 else 0.0, success).also {
        set("resource", it)
        if (it >= 70) set("cloning", true)
    }
}

data class MyHopMetric(val step: Double = 1.0) : DistanceSensor {
    override fun <ID : Any> Aggregate<ID>.distances(): Field<ID, Double> =
        neighboring(step)
            .mapWithId { id, value ->
            if (id == localId) 0.0 else value + step
        }
}
