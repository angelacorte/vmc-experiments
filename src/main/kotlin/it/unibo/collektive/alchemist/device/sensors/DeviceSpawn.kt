package it.unibo.collektive.alchemist.device.sensors

import it.unibo.collektive.aggregate.api.Aggregate

interface DeviceSpawn {
    fun spawn(coordinate: Pair<Double, Double>): Double

    fun <ID : Comparable<ID>> Aggregate<ID>.selfDestroy()

    fun currentTime(): Double

    val cloningRange: Double

    val maxChildren: Int

    val minSpawnWait: Double
}
