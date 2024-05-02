package it.unibo.collektive.lib

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.SuccessSensor
import it.unibo.collektive.coordination.convergeCast

context(SuccessSensor)
fun <ID : Comparable<ID>> Aggregate<ID>.convergeSuccess(potential: Double, localSuccess: Double): Double =
    convergeCast(potential, localSuccess) { a, b -> a + b }.also { setSuccess(it) }

context(SuccessSensor)
fun obtainLocalSuccess(): Double =
    getLocalSuccess().also { setLocalSuccess(it) }

// context(SuccessSensor)
// fun applyNegativeForce(localSuccess: Double): Double =
//     localSuccess - (localSuccess * negativeForce)
