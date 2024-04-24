package it.unibo.alchemist.actions

import it.unibo.alchemist.model.Layer
import it.unibo.alchemist.model.Position2D
import it.unibo.alchemist.util.math.BidimensionalGaussian

class SuccessSourceLayer<P: Position2D<P>> @JvmOverloads constructor(
    centerX: Double,
    centerY: Double,
    norm: Double,
    sigmaX: Double,
    sigmaY: Double = sigmaX,
) : Layer<Double, P> {
    private val function = BidimensionalGaussian(norm, centerX, centerY, sigmaX, sigmaY)

    override fun getValue(p: P): Double = function.value(p.x, p.y)
}