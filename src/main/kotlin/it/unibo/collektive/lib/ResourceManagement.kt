package it.unibo.collektive.lib

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.LeaderSensor
import it.unibo.collektive.alchemist.device.sensors.LocationSensor
import it.unibo.collektive.alchemist.device.sensors.ResourceSensor
import it.unibo.collektive.coordination.spreadToChildren
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

context(ResourceSensor, LeaderSensor, LocationSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.spreadResource(
    potential: Double,
    localSuccess: Double,
    isLeader: Boolean,
): Double {
    return spreadToChildren(potential, generateResource(isLeader), localSuccess).also {
        updateResource(it)
    }
}

context(LocationSensor, ResourceSensor, LeaderSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.generateResource(isLeader: Boolean): Double =
    when {
        isLeader -> {
            val finalResource = displacements()
                .map { sqrt(it.first.pow(2.0) + it.second.pow(2.0)) }
                .filterNot { it == 0.0 }
                .fold(leaderInitialResource) { acc, distance ->
                    acc - (acc * (1.0 - (distance / evaluationRadius).absoluteValue)) //TODO check if this formula is correct
                }
            if (finalResource > 0.0) finalResource else defaultResource
        }
        else -> defaultResource
    }.also {
        set("resource", it)
    }
