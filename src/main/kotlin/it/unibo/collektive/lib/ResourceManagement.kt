package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.DeviceSpawn
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.LocationSensor
import it.unibo.collektive.alchemist.device.sensors.SelfDestroy
import it.unibo.collektive.coordination.spreadToChildren
import it.unibo.collektive.field.Field

context(DistanceSensor, EnvironmentVariables, DeviceSpawn, SelfDestroy, LocationSensor)
fun <ID : Comparable<ID>> Aggregate<ID>.spreadResource(
    potential: Double,
    localSuccess: Double,
): Double {
    val condition = if (getOrDefault("leader", false)) {
        getOrDefault("leaderInitialResource", 500.0)
    } else 0.0
    return spreadToChildren(potential, condition, localSuccess).also {
        set("resource", it)
        val neighbors = neighboring(coordinates())
        spawnIfNeeded(neighbors, spawnWithRatio() && spawnWithChildsNumber())
        selfDestroyIfNeeded(destroyWithRatio())
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

context(EnvironmentVariables, DeviceSpawn)
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