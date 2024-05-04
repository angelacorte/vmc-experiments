package it.unibo.collektive.alchemist.device.sensors.impl

import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.NodeProperty
import it.unibo.alchemist.model.Position
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.collektive.alchemist.device.sensors.ResourceSensor

class ResourceSensorProperty<T, P : Position<P>>(
    private val environment: Environment<T, P>,
    override val node: Node<T>,
    override val leaderInitialResource: Double,
) : ResourceSensor, NodeProperty<T> {
    private val resourceMolecule = SimpleMolecule("resource")

    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> =
        ResourceSensorProperty(environment, node, leaderInitialResource)

    override fun getResource(): Double =
        node.getConcentration(resourceMolecule) as Double

    @Suppress("UNCHECKED_CAST")
    override fun updateResource(resource: Double) =
        node.setConcentration(resourceMolecule, resource as T)

    override fun setInitialResource(source: Boolean) =
        if (source) updateResource(leaderInitialResource) else updateResource(0.0)
}
