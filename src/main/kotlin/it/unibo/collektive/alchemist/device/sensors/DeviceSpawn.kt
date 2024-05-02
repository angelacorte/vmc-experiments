package it.unibo.collektive.alchemist.device.sensors

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.field.Field

interface DeviceSpawn {
    fun <ID : Comparable<ID>> Aggregate<ID>.spawn(coordinate: Pair<Double, Double>)

    fun <ID : Comparable<ID>> Aggregate<ID>.destroy()

//    fun <ID : Any> Aggregate<ID>.spawnWithRatio(): Boolean
//
//    fun spawnWithChildsNumber(): Boolean
}
