package it.unibo.collektive.alchemist.device.sensors.impl

import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.NodeProperty
import it.unibo.alchemist.model.Position
import it.unibo.collektive.alchemist.device.sensors.LocationSensor

class LocationSensorProperty<T, P : Position<P>>(
    private val environment: Environment<T, P>,
    override val node: Node<T>,
) : LocationSensor, NodeProperty<T> {
    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> =
        LocationSensorProperty(environment, node)

    override fun coordinates(): Pair<Double, Double> {
        val position = environment.getPosition(node).coordinates
        return Pair(position[0], position[1])
    }
}
