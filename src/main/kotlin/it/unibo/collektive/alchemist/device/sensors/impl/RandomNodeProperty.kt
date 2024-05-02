package it.unibo.collektive.alchemist.device.sensors.impl

import it.unibo.alchemist.model.Node
import it.unibo.alchemist.model.NodeProperty
import it.unibo.alchemist.util.RandomGenerators.nextDouble
import it.unibo.collektive.alchemist.device.sensors.RandomSuccess
import org.apache.commons.math3.random.RandomGenerator

class RandomNodeProperty<T>(
    override val node: Node<T>,
    private val rg: RandomGenerator,
): RandomSuccess, NodeProperty<T> {
    override fun cloneOnNewNode(node: Node<T>): NodeProperty<T> = RandomNodeProperty(node, rg)

    override fun nextRandomDouble(): Double = rg.nextDouble()

    override fun nextRandomDouble(until: Double): Double = rg.nextDouble(0.0, until)

    override fun  nextRandomDouble(range: ClosedFloatingPointRange<Double>): Double =
        rg.nextDouble(range.start, range.endInclusive)
}