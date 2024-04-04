package it.unibo.collektive.vmc

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.field.Field
import it.unibo.collektive.field.plus
import it.unibo.collektive.vmc.costants.alpha
import it.unibo.collektive.vmc.costants.beta
import it.unibo.collektive.vmc.costants.c
import it.unibo.collektive.vmc.costants.pConst
import it.unibo.collektive.vmc.costants.wConst
import it.unibo.collektive.vmc.sensors.LightSensor
import java.lang.Double.max
import kotlin.math.min

fun Aggregate<Int>.VMC(): Double {
    exchanging(0.0) {
        val successin = it + successinAtLeaf()
        successin.yielding { it.map { _ -> it.successinAtJunction() } }
    }
    return Double.POSITIVE_INFINITY
}

// Successin (S)

// Vascular pathway of resource (V)

// pathways are used to distribute a limited resource (R)

// the limited resource is initiated at the root and flows towards the leaves being split at every branching point
// according to the thickness of the vascular pathways.

// The amount of Successins can be altered in their way from the leaves to the root, based on a set of parameters and the sensor values at the internal nodes.

// The regulation of the vascular pathways at the nodes is based on the value of Successin passing the node as well as a set of parameters.

// Successin S_i is produced at a leaf i based on the local sensor values and parameters of the algorithm as follows:

val sensors = listOf(LightSensor())

/**
 * "Successin" is produced at the leaves, based on the local sensory inputs and constant parameters.
 */
fun successinAtLeaf(): Double = wConst + sensors.sumOf { it.weight * it.input }

/**
 * "Successin" flows towards the root.
 * At an internal node, the flow of Successin is influenced by the inputs from the local sensor and constant parameters
 * via a transfer function in the range of [0,1].
 */
fun Field<Int, Double>.successinAtJunction(): Double = sigmoid() * this.localValue

private fun sigmoid(): Double = pConst + sensors.sumOf { it.transferRate * it.input }

fun updatingVessels(successin: Double, pathway: Double): Double =
    if(successin >= pathway) {
        min(successin, (1 - c) * pathway + beta + alpha * (successin - pathway))
    } else {
        max(successin, (1 - c) * pathway)
    }

fun distributingResource(): Double {
    return Double.POSITIVE_INFINITY
}
