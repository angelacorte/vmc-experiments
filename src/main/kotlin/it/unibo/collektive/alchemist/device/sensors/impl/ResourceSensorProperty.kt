package it.unibo.collektive.alchemist.device.sensors.impl

import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.NodeProperty
import it.unibo.alchemist.model.Position
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.collektive.alchemist.device.sensors.ResourceSensor

class ResourceSensorProperty<T, P: Position<P>>(
    override val leaderInitialResource: Double,
    private val environment: Environment<T, P>,
    override val node: Node<T>,
): ResourceSensor, NodeProperty<T> {
    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> =
        ResourceSensorProperty(leaderInitialResource, environment, node)

    override fun getResource(): Double =
        node.getConcentration(SimpleMolecule("resource")) as Double

    @Suppress("UNCHECKED_CAST")
    override fun updateResource(resource: Double) =
        node.setConcentration(SimpleMolecule("resource"), resource as T)

    override fun setInitialResource(source: Boolean) =
        if (source) updateResource(leaderInitialResource) else updateResource(0.0)
}