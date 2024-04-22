package it.unibo.alchemist.actions

import it.unibo.alchemist.model.Action
import it.unibo.alchemist.model.Context
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.Position
import it.unibo.alchemist.model.Reaction
import it.unibo.alchemist.model.actions.AbstractAction
import it.unibo.alchemist.model.molecules.SimpleMolecule
import org.apache.commons.math3.random.RandomGenerator

@Suppress("UNCHECKED_CAST")
class CloningAction<T, P : Position<out P>>(
    node: Node<T>,
    val environment: Environment<T, P>,
    val randomGenerator: RandomGenerator
) : AbstractAction<T>(node) {
    override fun cloneAction(node: Node<T>?, reaction: Reaction<T>?): Action<T> {
        return CloningAction(node!!, environment, randomGenerator)
    }

    override fun execute() {
        val cloningMolecule = SimpleMolecule("cloning")
        if(node.contains(cloningMolecule) && node.contents[cloningMolecule] == true) {
            node.removeConcentration(cloningMolecule)

            val cloneOfThis = node.cloneNode(environment.simulation.time)
            node.reactions.forEach { it.cloneOnNewNode(cloneOfThis, environment.simulation.time) }
            node.contents.forEach { cloneOfThis.removeConcentration(it.key) }
            node.properties.forEach { it.cloneOnNewNode(cloneOfThis) }
            val position = environment.getPosition(this.node)
            val coordinates =
                position.coordinates.map { it + (0.5 - randomGenerator.nextDouble()) * 2 }
            val updatedPosition = environment.makePosition(*coordinates.toTypedArray())
            environment.addNode(cloneOfThis, updatedPosition)
        }
    }

    override fun getContext(): Context = Context.NEIGHBORHOOD
}
