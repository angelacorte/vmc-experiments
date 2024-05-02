package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.DeviceSpawn
import it.unibo.collektive.alchemist.device.sensors.LeaderSensor
import it.unibo.collektive.alchemist.device.sensors.LocationSensor
import it.unibo.collektive.alchemist.device.sensors.ResourceSensor
import it.unibo.collektive.alchemist.device.sensors.SelfDestroy
import it.unibo.collektive.coordination.spreadToChildren

context(DistanceSensor, DeviceSpawn, SelfDestroy, LocationSensor, ResourceSensor, LeaderSensor)
fun <ID : Comparable<ID>> Aggregate<ID>.spreadResource(
    potential: Double,
    localSuccess: Double,
): Double {
    setInitialResource(isLeader())
    return spreadToChildren(potential, getResource(), localSuccess).also {
        updateResource(it)
        val neighbors = neighboring(coordinates())
        spawnIfNeeded(neighbors, spawnWithRatio() && spawnWithChildsNumber())
        selfDestroyIfNeeded(destroyWithRatio())
    }
}
