package it.unibo.collektive.vmc

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.EnvironmentLayer
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.lib.chooseLeader
import it.unibo.collektive.lib.convergeSuccess
import it.unibo.collektive.lib.findPotential
import it.unibo.collektive.lib.obtainLocalSuccess
import it.unibo.collektive.lib.spreadResource

context(EnvironmentVariables, EnvironmentLayer, DistanceSensor)
fun Aggregate<Int>.vmcSource(): Double {
    val isLeader = chooseLeader()
    val potential = findPotential(isLeader)
    val localSuccess = obtainLocalSuccess()
    val success = convergeSuccess(potential, localSuccess)
    return spreadResource(potential, success)
}