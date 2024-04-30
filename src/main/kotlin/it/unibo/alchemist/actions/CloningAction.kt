package it.unibo.alchemist.actions

import io.kotest.mpp.env
import it.unibo.alchemist.model.Action
import it.unibo.alchemist.model.Context
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.Position
import it.unibo.alchemist.model.Reaction
import it.unibo.alchemist.model.actions.AbstractAction
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.alchemist.model.times.DoubleTime
import it.unibo.alchemist.util.RandomGenerators.nextDouble
import org.apache.commons.math3.random.RandomGenerator
import kotlin.math.nextUp

class CloningAction<T, P : Position<out P>>(
    node: Node<T>,
    private val environment: Environment<T, P>,
    private val randomGenerator: RandomGenerator
) : AbstractAction<T>(node) {
    override fun cloneAction(node: Node<T>?, reaction: Reaction<T>?): Action<T> {
        return CloningAction(node!!, environment, randomGenerator)
    }

    override fun execute() {
        val cloningMolecule = SimpleMolecule("cloning")
        val removeMolecule = SimpleMolecule("remove")
        if(node.contains(cloningMolecule) && node.contents[cloningMolecule] == true) {
            node.removeConcentration(cloningMolecule)
            val cloneOfThis = node.cloneNode(environment.simulation.time + DoubleTime(randomGenerator.nextDouble(0.0.nextUp(), 0.1)))
            val position = environment.getPosition(this.node)
            val coordinates =
                position.coordinates.map { it + (0.5 - randomGenerator.nextDouble()) * 5 }
            val updatedPosition = environment.makePosition(*coordinates.toTypedArray())
            environment.addNode(cloneOfThis, updatedPosition)
        }
        if(node.contains(removeMolecule) && node.contents[removeMolecule] == true) {
            environment.removeNode(node)
        }
    }

    override fun getContext(): Context = Context.NEIGHBORHOOD
}
