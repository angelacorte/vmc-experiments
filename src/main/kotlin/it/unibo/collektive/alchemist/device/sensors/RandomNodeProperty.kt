package it.unibo.collektive.alchemist.device.sensors

import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.NodeProperty
import kotlin.random.Random

class RandomNodeProperty<T> (override val node: Node<T>): RandomGenerator, NodeProperty<T> {

    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> = RandomNodeProperty(node)

    override fun nextRandomDouble(): Double = Random.nextDouble()

    override fun nextRandomDouble(until: Double): Double = Random.nextDouble(until)

    override fun  nextRandomDouble(range: ClosedFloatingPointRange<Double>): Double =
        Random.nextDouble(range.start, range.endInclusive)
}