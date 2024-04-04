package it.unibo.collektive.vmc.sensors

import kotlin.random.Random

sealed interface Sensor{
    val weight: Double
    val input: Double
    val transferRate: Double
}
