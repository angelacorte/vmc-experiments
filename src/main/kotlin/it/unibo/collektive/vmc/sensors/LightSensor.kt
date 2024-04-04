package it.unibo.collektive.vmc.sensors

import it.unibo.collektive.vmc.utils.generateRandomInput
import it.unibo.collektive.vmc.utils.generateRandomTransferRate
import it.unibo.collektive.vmc.costants.wLight


class LightSensor(
    override val weight: Double = wLight,
): Sensor {
    override var input = generateRandomInput()
    override var transferRate = generateRandomTransferRate()
}
