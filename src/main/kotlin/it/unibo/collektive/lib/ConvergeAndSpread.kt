package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.CollektiveDevice
import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.alchemist.model.Position
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.RandomGenerator
import it.unibo.collektive.field.Field

context(RandomGenerator, CollektiveDevice<*>)
fun Aggregate<Int>.convergeAndSpread(): Double {
    localId.set("ids $localId", id)
    val leader = with(MyHopMetric()) {
        boundedElection(localId, 12.0) //localId == 0 //
    }
    localId.set("source", leader)
    val potential = with(MyHopMetric()) {
        distanceTo(leader)
    }
    localId.set("potential", potential)
//    val localSuccess = max(0, 20 - neighboring(1).hood(0) { a, b -> a + b})
    val localSuccess = nextRandomDouble(0.0..20.0)
    localId.set("localSuccess", localSuccess)
    val success = convergeCast(potential, localSuccess) { a, b -> a + b }
    localId.set("success", success)
    return spreadToChildren(potential, if (leader) 500.0 else 0.0, success).also {
        localId.set("resource", it)
        if (it >= 400) {
            val cloned = repeat(1) { a -> a + 1 }
            if(cloned in 10..14) {
                localId.set("cloning", true)
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
