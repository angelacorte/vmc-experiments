package it.unibo.collektive.alchemist.device.sensors

import it.unibo.collektive.aggregate.api.Aggregate

interface SelfDestroy {
    fun selfDestroyIfNeeded(destroy: Boolean)

    fun <ID : Any> Aggregate<ID>.destroyWithRatio(): Boolean
}