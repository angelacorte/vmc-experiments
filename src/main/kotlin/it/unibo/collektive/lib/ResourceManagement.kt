package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.DeviceSpawner
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.SelfDestroy
import it.unibo.collektive.coordination.boundedElection
import it.unibo.collektive.coordination.distanceTo
import it.unibo.collektive.coordination.spreadToChildren
import it.unibo.collektive.field.Field

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

context(DistanceSensor, EnvironmentVariables, DeviceSpawner, SelfDestroy)
fun <ID : Comparable<ID>> Aggregate<ID>.spreadResource(
    potential: Double,
    localSuccess: Double,
): Double {
    val condition = if (getOrDefault("leader", false)) {
        getOrDefault("leaderInitialResource", 500.0)
    } else 0.0
    return spreadToChildren(potential, condition, localSuccess).also {
        spawnIfNeeded()
        selfDestroyIfNeeded()
//        set("resource", it)
//        val neighbors = neighboring(coordinates())
//        if (it >= getOrDefault("resourceUpperBound", 50.0) &&
//            get<Double>("localSuccess") >= getOrDefault("successBound", 10.0)
//        ){
//            cloneNode(neighbors)
//        }
//        if (it <= getOrDefault("resourceLowerBound", 2.0)) {
//            deleteNode()
//        }

    }
}

private fun <ID : Comparable<ID>> Aggregate<ID>.neighbors(): Field<ID, ID> = neighboring(localId)

context(DistanceSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.applyNegativeForce(localSuccess: Double): Double {
    val negativeForce = getOrDefault("negativeForce", 0.5)
    return localSuccess - (localSuccess * negativeForce)
}

context(EnvironmentVariables, DeviceSpawner)
fun <ID : Comparable<ID>> Aggregate<ID>.cloneNode(neighbors: Field<ID, Pair<Double, Double>>) {
    val cloned = repeat(1) { a -> a + 1 }
    if (cloned % getOrDefault("cloningRatio", 500.0) == 0.0) { // && cloned in 500 .. 1500
        set("cloning", neighbors)
    }
}

context(EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.deleteNode(){
    val span = repeat(1) { a -> a + 1 }
    if (span % getOrDefault("deletingRatio", 10.0) == 0.0) set("deleting", true)
}