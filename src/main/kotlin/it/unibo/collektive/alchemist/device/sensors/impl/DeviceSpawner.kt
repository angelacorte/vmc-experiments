package it.unibo.collektive.alchemist.device.sensors.impl

import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.NodeProperty
import it.unibo.alchemist.model.Position
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.alchemist.model.times.DoubleTime
import it.unibo.alchemist.util.RandomGenerators.nextDouble
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.DeviceSpawn
import it.unibo.collektive.field.Field
import it.unibo.collektive.field.Field.Companion.hood
import org.apache.commons.math3.random.RandomGenerator
import kotlin.math.nextUp

class DeviceSpawner<T, P: Position<P>>(
    private val randomGenerator: RandomGenerator,
    private val environment: Environment<T, P>,
    override val node: Node<T>,
): DeviceSpawn, NodeProperty<T> {
    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> =
        DeviceSpawner(randomGenerator, environment, node)

    override fun <ID : Comparable<ID>> Aggregate<ID>.spawn(coordinate: Pair<Double, Double>) {
        val cloneOfThis = node.cloneNode(
            environment.simulation.time + DoubleTime(randomGenerator.nextDouble(0.0.nextUp(), 0.1))
        )
        val updatedPosition = environment.makePosition(*coordinate.toList().toTypedArray())
        environment.addNode(cloneOfThis, updatedPosition)
    }

    override fun <ID : Comparable<ID>> Aggregate<ID>.destroy() {
        node.reactions.toList().forEach {
            environment.simulation.reactionRemoved(it)
            node.removeReaction(it)
        }
        environment.simulation.schedule { environment.removeNode(node) }
//        node.reactions.forEach {
//                    environment.simulation.reactionRemoved(it)
//                    node.removeReaction(it)
//                    environment.simulation.schedule {  }
//        }
    }

    operator fun Pair<Double, Double>.minus(other: Pair<Double, Double>): Pair<Double, Double> =
        first - other.first to second - other.second

    operator fun Pair<Double, Double>.plus(other: Pair<Double, Double>): Pair<Double, Double> =
        first + other.first to second + other.second

}
