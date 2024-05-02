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
import org.apache.commons.math3.random.RandomGenerator
import kotlin.math.nextUp

class DeviceSpawner<T, P: Position<P>>(
    private val ratio: Double,
    private val resourceUpperBound: Double,
    private val maxChilds: Int,
    private val environment: Environment<T, P>,
    override val node: Node<T>,
    private val randomGenerator: RandomGenerator,
): DeviceSpawn, NodeProperty<T> {
    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> =
        DeviceSpawner(ratio, resourceUpperBound, maxChilds, environment, node, randomGenerator)

    @Suppress("UNCHECKED_CAST")
    override fun <ID : Any> spawnIfNeeded(neighbors: Field<ID, Pair<Double, Double>>, spawn: Boolean) {
        val resourceMolecule = SimpleMolecule("resource")
        val childsMolecule = SimpleMolecule("childs")
        if( spawn && node.contains(resourceMolecule) &&
            node.getConcentration(resourceMolecule) as Double >= resourceUpperBound ) {
            val nodePosition = environment.getPosition(node).coordinates
            val vector = neighbors.averageDistanceVector()
            val cloneOfThis = node.cloneNode(
                environment.simulation.time + DoubleTime(randomGenerator.nextDouble(0.0.nextUp(), 0.1))
            )
            if(cloneOfThis.contains(childsMolecule)) cloneOfThis.setConcentration(childsMolecule, 0.0 as T)
            val updatedPosition = environment.makePosition(nodePosition[0] + vector.first, nodePosition[1] + vector.second)
            environment.addNode(cloneOfThis, updatedPosition)
        }
    }

    override fun <ID : Any> Aggregate<ID>.spawnWithRatio(): Boolean =
        repeat(1) {a -> a + 1} % ratio == 0.0

    @Suppress("UNCHECKED_CAST")
    override fun spawnWithChildsNumber(): Boolean {
        val childsMolecule = SimpleMolecule("childs")
        if(node.contains(childsMolecule)) {
            node.setConcentration(childsMolecule, (node.getConcentration(childsMolecule) as Double + 1.0) as T)
        } else {
            node.setConcentration(childsMolecule, 1.0 as T)
        }
        val childs = node.getConcentration(childsMolecule) as Double
        return childs < maxChilds
    }

    private fun <ID: Any> Field<ID, Pair<Double, Double>>.averageDistanceVector(): Pair<Double, Double>{
        val x = ((0.5 - randomGenerator.nextDouble()) * 5)
        val y = ((0.5 - randomGenerator.nextDouble()) * 5)
//        val vectorSum = this.map { neighPos ->
//            myPosition.first - neighPos.first to myPosition.second - neighPos.second
//        }.hood(x to y ) { acc, pair ->
//            acc.first + pair.first to acc.second + pair.second
//        }.let { if(it.first == 0.0 && it.second == 0.0) return x to y else it }

        //val length = vectorSum.let { sqrt(it.first * it.first + it.second * it.second) }
        return x to y //vectorSum.let { it.first / length to it.second / length }
    }
}
