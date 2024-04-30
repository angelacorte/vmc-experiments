package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentLayer
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.coordination.boundedElection
import it.unibo.collektive.coordination.convergeCast
import it.unibo.collektive.coordination.distanceTo
import it.unibo.collektive.coordination.spreadToChildren

context(DistanceSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.chooseLeader(): Boolean =
    boundedElection(localId, getOrDefault("leaderRadius", 12.0))
        .also { set("leaderId", it) }
        .let { it == localId }
        .also { set("leader", it) }

context(DistanceSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.findPotential(leader: Boolean): Double {
    val potential = distanceTo(leader)
    return set ("potential", potential)
}

context(DistanceSensor, EnvironmentVariables, EnvironmentLayer)
fun <ID : Comparable<ID>> Aggregate<ID>.obtainLocalSuccess(): Double {
    val localSuccess = getFromLayer<Double>("successSource")
    return set("localSuccess", localSuccess)
}

context(DistanceSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.convergeSuccess(potential: Double, localSuccess: Double): Double {
    val success = convergeCast(potential, localSuccess) { a, b -> a + b }
    return set("success", success)
}

context(DistanceSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.spreadResource(potential: Double, localSuccess: Double): Double {
    val condition = if (getOrDefault("leader", false)) {
        getOrDefault("leaderInitialResource", 500.0)
    } else 0.0
    return spreadToChildren(potential, condition, localSuccess).also {
        set("resource", it)
        if (it >= getOrDefault("resourceUpperBound", 200.0)) {
            cloneNode()
        }
        if (it <= getOrDefault("resourceLowerBound", 10.0)) {
            deleteNode()
        }
    }
}

context(DistanceSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.applyNegativeForce(localSuccess: Double): Double {
    val negativeForce = getOrDefault("negativeForce", 0.5)
    return localSuccess - (localSuccess * negativeForce)
}

context(EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.cloneNode() {
    val cloned = repeat(1) { a -> a + 1 }
    if (cloned % getOrDefault("cloningRatio", 500.0) == 0.0) set("cloning", true)
}

context(EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.deleteNode(){
    val span = repeat(1) { a -> a + 1 }
    if (span % getOrDefault("deletingRatio", 10.0) == 0.0) set("deleting", true)
}