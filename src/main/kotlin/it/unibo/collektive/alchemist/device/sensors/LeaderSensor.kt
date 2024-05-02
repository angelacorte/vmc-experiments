package it.unibo.collektive.alchemist.device.sensors

interface LeaderSensor {
    val leaderRadius: Double

    fun isLeader(): Boolean

    fun setLeader(leader: Boolean)

    fun <ID : Any> setLeaderId(id: ID)
}
