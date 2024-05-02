package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.coordination.boundedElection
import it.unibo.collektive.coordination.distanceTo

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