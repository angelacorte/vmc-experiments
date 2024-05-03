package it.unibo.collektive.lib

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.ResourceSensor
import it.unibo.collektive.coordination.spreadToChildren

context(ResourceSensor)
fun <ID : Comparable<ID>> Aggregate<ID>.spreadResource(
    potential: Double,
    localSuccess: Double,
    isLeader: Boolean,
): Double {
    setInitialResource(isLeader)
    return spreadToChildren(potential, getResource(), localSuccess).also {
        updateResource(it)
    }
}
