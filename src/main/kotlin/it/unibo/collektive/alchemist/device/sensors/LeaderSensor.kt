package it.unibo.collektive.alchemist.device.sensors

interface LeaderSensor {
    fun isLeader(): Boolean

    fun setLeader(leader: Boolean)
}