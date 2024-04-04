package it.unibo.collektive.vmc.utils

import kotlin.random.Random

fun generateRandomInput(): Double = Random.nextDouble(0.0,1.0)

fun generateRandomTransferRate(): Double = Random.nextDouble(-1.0, 1.0)
