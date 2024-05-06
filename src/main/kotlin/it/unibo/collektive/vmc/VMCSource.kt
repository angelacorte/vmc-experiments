package it.unibo.collektive.vmc

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.DeviceSpawn
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.LeaderSensor
import it.unibo.collektive.alchemist.device.sensors.LocationSensor
import it.unibo.collektive.alchemist.device.sensors.RandomSuccess
import it.unibo.collektive.alchemist.device.sensors.ResourceSensor
import it.unibo.collektive.alchemist.device.sensors.SuccessSensor
import it.unibo.collektive.coordination.findParent
import it.unibo.collektive.field.Field.Companion.fold
import it.unibo.collektive.lib.convergeSuccess
import it.unibo.collektive.lib.findPotential
import it.unibo.collektive.lib.isLeader
import it.unibo.collektive.lib.metrics.MyHopMetric
import it.unibo.collektive.lib.obtainLocalSuccess
import it.unibo.collektive.lib.spreadResource
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.absoluteValue
import kotlin.math.withSign

context(
    EnvironmentVariables,
    DeviceSpawn,
    LocationSensor,
    LeaderSensor,
    ResourceSensor,
    SuccessSensor,
    RandomSuccess
)
fun Aggregate<Int>.vmcSource(): Double = predappio()

context(
    EnvironmentVariables,
    DeviceSpawn,
    LocationSensor,
    LeaderSensor,
    ResourceSensor,
    SuccessSensor,
    RandomSuccess
)
@JvmOverloads
fun Aggregate<Int>.predappio(
    resourceLowerBound: Double = 20.0,
    certainSpawnThreshold: Double = 100.0,
    maxChildren: Int = 1,
    minSpawnWait: Double = 100.0,
): Double = with(MyHopMetric()) {
    vmc { potential: Double, _: Double, _: Double, localResource: Double ->
        val children = neighboring(findParent(potential))
            .fold(0) { acc, parent -> acc + if (parent == localId) 1 else 0 }
        val neighbors = neighboring(coordinates())
        val localPosition = neighbors.localValue
        repeat(currentTime()) { time ->
            val enoughTime = currentTime() > time + minSpawnWait
            when {
                !enoughTime -> time
                nextRandomDouble(0.0..certainSpawnThreshold) < localResource && children < maxChildren -> {
                    val relativeDestination = neighbors.map { it - localPosition }
                        .fold((0.5 - nextRandomDouble()) * 1.2 to (0.5 - nextRandomDouble()) * 1.2) { acc, pair -> acc + pair }
                    val absoluteDestination = localPosition - relativeDestination
                    val finalCoordinates = when {
                        relativeDestination.first.absoluteValue > cloningRange -> cloningRange.withSign(relativeDestination.first)
                        else -> absoluteDestination.first
                    } to when {
                        relativeDestination.second.absoluteValue > cloningRange -> cloningRange.withSign(relativeDestination.second)
                        else -> absoluteDestination.second
                    } //todo must implement this with atan2 ecc
                    spawn(finalCoordinates)
                }
                nextRandomDouble(0.0..resourceLowerBound) > localResource -> {
                    selfDestroy()
                    POSITIVE_INFINITY
                }
                else -> time
            }
        }
    }
}

context(
    EnvironmentVariables,
    DistanceSensor,
    DeviceSpawn,
    LocationSensor,
    LeaderSensor,
    ResourceSensor,
    SuccessSensor
)
fun Aggregate<Int>.vmc(spawner: Spawner): Double {
    val isLeader = isLeader()
    val potential = findPotential(isLeader)
    val localSuccess = obtainLocalSuccess()
    val success = convergeSuccess(potential, localSuccess)
    val localResource = spreadResource(potential, success, isLeader)
    spawner(this@DeviceSpawn, this@LocationSensor, this, potential, localSuccess, success, localResource)
    return localResource
}

typealias Spawner = context(DeviceSpawn, LocationSensor) Aggregate<Int>.(potential: Double, localSuccess: Double, success: Double, localResource: Double) -> Unit

operator fun Pair<Double, Double>.minus(other: Pair<Double, Double>): Pair<Double, Double> =
    first - other.first to second - other.second
operator fun Pair<Double, Double>.plus(other: Pair<Double, Double>): Pair<Double, Double> =
    first + other.first to second + other.second
