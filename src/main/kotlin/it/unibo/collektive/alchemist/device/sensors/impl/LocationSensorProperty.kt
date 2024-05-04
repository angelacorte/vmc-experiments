package it.unibo.collektive.alchemist.device.sensors.impl

import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.NodeProperty
import it.unibo.alchemist.model.Position
import it.unibo.alchemist.model.molecules.SimpleMolecule
import it.unibo.collektive.alchemist.device.sensors.LocationSensor

class LocationSensorProperty<T : Any, P : Position<P>>(
    private val environment: Environment<T, P>,
    override val node: Node<T>,
    override val evaluationRadius: Double,
) : LocationSensor, NodeProperty<T> {
    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> =
        LocationSensorProperty(environment, node, evaluationRadius)

    override fun coordinates(): Pair<Double, Double> {
        val position = environment.getPosition(node).coordinates
        return Pair(position[0], position[1])
    }

    override fun displacements(): List<Pair<Double, Double>> {
        val myPos = environment.getPosition(node).coordinates
        return neighborhoodInsideRadius().filter { n ->
            n.getConcentration(SimpleMolecule("leader")) as Boolean
        }.map { n ->
            val nPos = environment.getPosition(n).coordinates
            Pair(myPos[0] - nPos[0], myPos[1] - nPos[1])
        }
    }

    private fun neighborhoodInsideRadius(): List<Node<T>> =
        environment.getPosition(node).let {
            environment.nodes.filter { n ->
                environment.getPosition(n).distanceTo(it) <= evaluationRadius
            }
        }
}
