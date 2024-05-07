package it.unibo.collektive.lib

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.LeaderSensor
import it.unibo.collektive.alchemist.device.sensors.ResourceSensor
import it.unibo.collektive.coordination.boundedElection
import it.unibo.collektive.coordination.distanceTo

context(DistanceSensor, LeaderSensor, ResourceSensor)
fun <ID : Comparable<ID>> Aggregate<ID>.chooseLeader(): ID =
    boundedElection(getResource(), leaderRadius)

context(DistanceSensor)
fun <ID : Comparable<ID>> Aggregate<ID>.findPotential(leader: Boolean): Double =
    distanceTo(leader)

context(DistanceSensor, LeaderSensor, EnvironmentVariables, ResourceSensor)
fun <ID : Comparable<ID>> Aggregate<ID>.isLeader(): Boolean =
    (chooseLeader() == localId).also { setLeader(it) }
