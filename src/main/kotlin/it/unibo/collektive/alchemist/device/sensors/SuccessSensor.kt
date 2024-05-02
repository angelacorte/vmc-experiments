package it.unibo.collektive.alchemist.device.sensors

interface SuccessSensor {
    fun setSuccess(success: Double)

    fun setLocalSuccess(localSuccess: Double)

    fun getSuccess(): Double

    fun getLocalSuccess(): Double
}
