package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.LeaderSensor
import it.unibo.collektive.coordination.boundedElection
import it.unibo.collektive.coordination.distanceTo

context(DistanceSensor, LeaderSensor)
fun <ID : Comparable<ID>> Aggregate<ID>.chooseLeader(): Boolean =
    boundedElection(localId, leaderRadius)
        .also { setLeaderId(it) }
        .let { it == localId }
        .also { setLeader(it) }

context(DistanceSensor, EnvironmentVariables)
fun <ID : Comparable<ID>> Aggregate<ID>.findPotential(leader: Boolean): Double =
    distanceTo(leader).also { set("potential", it)}
