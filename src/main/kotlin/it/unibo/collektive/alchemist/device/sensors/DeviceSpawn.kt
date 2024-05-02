package it.unibo.collektive.alchemist.device.sensors

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.field.Field

interface DeviceSpawn {
    fun <ID : Any> spawnIfNeeded(neighbors: Field<ID, Pair<Double, Double>>, spawn: Boolean)

    fun <ID : Any> Aggregate<ID>.spawnWithRatio(): Boolean

    fun spawnWithChildsNumber(): Boolean
}
