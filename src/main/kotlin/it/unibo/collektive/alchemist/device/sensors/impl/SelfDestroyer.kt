package it.unibo.collektive.alchemist.device.sensors.impl

import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.NodeProperty
import it.unibo.alchemist.model.Position
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.SelfDestroy

class SelfDestroyer<T, P: Position<P>>(
    private val ratio: Double,
    private val resourceLowerBound: Double,
    private val environment: Environment<T, P>,
    override val node: Node<T>,
): SelfDestroy, NodeProperty<T> {
    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> =
        SelfDestroyer(ratio, resourceLowerBound, environment, node)

    override fun selfDestroyIfNeeded(destroy: Boolean) {
        val resourceMolecule = SimpleMolecule("resource")
        if( destroy && node.contains(resourceMolecule) &&
            node.getConcentration(resourceMolecule) as Double <= resourceLowerBound) {
                val all = node.reactions.toList()
                all.forEach { environment.simulation.reactionRemoved(it) }
                all.forEach { node.removeReaction(it) }
                environment.simulation.schedule { environment.removeNode(node) }
                    //environment.removeNode(node)
        }
    }

    override fun <ID : Any> Aggregate<ID>.destroyWithRatio(): Boolean =
        repeat(1) {a -> a + 1} % ratio == 0.0
}