@file:Suppress("UNCHECKED_CAST")

package it.unibo.collektive.alchemist.device.sensors.impl

import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.NodeProperty
import it.unibo.alchemist.model.Position
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.alchemist.util.RandomGenerators.nextDouble
import it.unibo.collektive.alchemist.device.sensors.SuccessSensor
import org.apache.commons.math3.random.RandomGenerator
import kotlin.jvm.optionals.getOrNull

class SuccessSensorProperty<T, P: Position<P>>(
    override val negativeForce: Double,
    private val maxSuccess: Double,
    override val node: Node<T>,
    private val environment: Environment<T, P>,
    private val random: RandomGenerator,
): SuccessSensor, NodeProperty<T> {
    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> =
        SuccessSensorProperty(negativeForce, maxSuccess, node, environment, random)

    override fun setSuccess(success: Double) =
        node.setConcentration(SimpleMolecule("success"), success as T)

    override fun setLocalSuccess(localSuccess: Double) =
        node.setConcentration(SimpleMolecule("localSuccess"), localSuccess as T)

    override fun getSuccess(): Double =
        node.getConcentration(SimpleMolecule("success")) as Double

    override fun getLocalSuccess(): Double =
        getFromLayer("successSource") ?: random.nextDouble(0.0, maxSuccess)

    private fun <T> getFromLayer(name: String): T =
        environment.getLayer(SimpleMolecule(name)).getOrNull()?.getValue(environment.getPosition(node)) as T
}