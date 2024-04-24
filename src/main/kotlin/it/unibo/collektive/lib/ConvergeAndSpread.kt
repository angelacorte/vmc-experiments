package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentLayer
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.field.Field

context(EnvironmentVariables, EnvironmentLayer)
fun Aggregate<Int>.convergeAndSpread(): Double = with(MyHopMetric()){
    val leader = boundedElection(localId, getOrDefault("leaderRadius", 12.0)) //localId == 0 //
    val potential = distanceTo(leader)
    set("source", leader)
    set("potential", potential)
    val localSuccess = getFromLayer<Double>("successSource")
    set("localSuccess", localSuccess)
    val success = convergeCast(potential, localSuccess) { a, b -> a + b }
    set("success", success)
    val condition = if (leader) {
        getOrDefault("initialResource", 500.0)
    } else 0.0
    spreadToChildren(potential, condition, success).also {
        set("resource", it)
        if (it >= getOrDefault("resourceTh", 200.0)) {
            val cloned = repeat(1) { a -> a + 1 }
            if(cloned % getOrDefault("cloningRatio", 500.0) == 0.0) {
                set("cloning", true)
            }
        }
    }
}

data class MyHopMetric(val step: Double = 1.0): DistanceSensor {
    override fun <ID : Any> Aggregate<ID>.distances(): Field<ID, Double> =
        neighboring(step)
            .mapWithId { id, value ->
            if (id == localId) 0.0 else value + step
        }
}
