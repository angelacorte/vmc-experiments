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
//        val localSuccessMolecule = SimpleMolecule("localSuccess")
//        val potentialMolecule = SimpleMolecule("potential")
//        val resourceMolecule = SimpleMolecule("resource")
//        val sourceMolecule = SimpleMolecule("source")
//        val successMolecule = SimpleMolecule("success")

        if(node.contents[cloningMolecule] == true) {
            node.removeConcentration(cloningMolecule)

            val cloneOfThis = node.cloneNode(environment.simulation.time)
//            node.reactions.forEach { it.cloneOnNewNode(cloneOfThis, environment.simulation.time) }
            node.reactions.forEach { r -> r.actions.forEach { a -> a.cloneAction(cloneOfThis, r) } }
            cloneOfThis.setConcentration(SimpleMolecule("SONOSTATOCLONATO"), true as T)
//            cloneOfThis.reactions.forEach { it.actions.forEach { b -> b.cloneAction(cloneOfThis, it) } }
            val position = environment.getPosition(this.node)
            val coordinates =
                position.coordinates.map { it + (0.5 - randomGenerator.nextDouble()) * 2 }.map { it }
            val updatedPosition = environment.makePosition(*coordinates.toTypedArray())
            environment.addNode(cloneOfThis, updatedPosition)
        }
    }

    override fun getContext(): Context = Context.NEIGHBORHOOD
}

/*
            cloneOfThis.setConcentration(cloningMolecule, false as T)
            cloneOfThis.setConcentration(localSuccessMolecule, 0.0 as T)
            cloneOfThis.setConcentration(potentialMolecule, 0.0 as T)
            cloneOfThis.setConcentration(resourceMolecule, 0.0 as T)
            cloneOfThis.setConcentration(sourceMolecule, false as T)
            cloneOfThis.setConcentration(successMolecule, 0.0 as T)
 */