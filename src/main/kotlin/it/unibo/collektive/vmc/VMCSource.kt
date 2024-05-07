package it.unibo.collektive.vmc

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.aggregate.api.operators.share
import it.unibo.collektive.alchemist.device.sensors.DeviceSpawn
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.LeaderSensor
import it.unibo.collektive.alchemist.device.sensors.LocationSensor
import it.unibo.collektive.alchemist.device.sensors.RandomSuccess
import it.unibo.collektive.alchemist.device.sensors.ResourceSensor
import it.unibo.collektive.alchemist.device.sensors.SuccessSensor
import it.unibo.collektive.coordination.findParent
import it.unibo.collektive.field.Field.Companion.fold
import it.unibo.collektive.field.Field.Companion.hood
import it.unibo.collektive.lib.convergeSuccess
import it.unibo.collektive.lib.findPotential
import it.unibo.collektive.lib.isLeader
import it.unibo.collektive.lib.metrics.MyHopMetric
import it.unibo.collektive.lib.obtainLocalSuccess
import it.unibo.collektive.lib.spreadResource
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

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
    resourceLowerBound: Double = 1.0,
//    certainSpawnThreshold: Double = 100.0,
    maxChildren: Int = 5,
    minSpawnWait: Double = 10.0,
): Double = with(MyHopMetric()) {
    vmc { potential: Double, localSuccess: Double, success: Double, localResource: Double ->
        val children = neighboring(findParent(potential))
            .fold(0) { acc, parent -> acc + if (parent == localId) 1 else 0 }
        val neighbors = neighboring(coordinates())
        val neighborSize: Int = neighbors.excludeSelf().size
        val localPosition = neighbors.localValue
        val relativeDestination = neighbors.map { it - localPosition }
            .hood(nextRandomDouble(-1.0..1.0) to nextRandomDouble(-1.0..1.0)) { acc, pair -> acc + pair }
        val now = currentTime()
        share(true) { neighborhoodIsStable ->
            val everyoneIsStable = neighborhoodIsStable.fold(true) { acc, change -> acc && change }
            val lastChanged = repeat(now to listOf(potential, localSuccess, success, localResource)) { last ->
                val current = listOf(potential, localSuccess, success, localResource)
                if (current == last.second) { last } else { now to current }
            }.first
            val enoughTime = now > lastChanged + minSpawnWait
            when {
                !enoughTime -> false
                potential > 0.0 && children == 0 && enoughTime && localResource < resourceLowerBound && everyoneIsStable -> {
                    selfDestroy()
                    false
                }
                localResource / (2 + children) > resourceLowerBound && children < maxChildren || neighborSize == 0 && everyoneIsStable -> {
                    val angle = PI +
                        atan2(relativeDestination.second, relativeDestination.first) +
                        nextRandomDouble(-PI / 6..PI / 6)
                    val norm = hypot(relativeDestination.first, relativeDestination.second)
                        .coerceIn(2 * cloningRange / 3, cloningRange)
                    val x = norm * cos(angle)
                    val y = norm * sin(angle)
                    val absoluteDestination = localPosition + (x to y)
//                    println("$localId@(${coordinates()}) -> Last changed: $lastChanged, potential: $potential")
                    spawn(absoluteDestination)
                    false
                }
                else -> true
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
    val localResource = spreadResource(potential, success)
    spawner(this@DeviceSpawn, this@LocationSensor, this, potential, localSuccess, success, localResource)
    return localResource
}

typealias Spawner = context(DeviceSpawn, LocationSensor) Aggregate<Int>.(potential: Double, localSuccess: Double, success: Double, localResource: Double) -> Unit

operator fun Pair<Double, Double>.minus(other: Pair<Double, Double>): Pair<Double, Double> =
    first - other.first to second - other.second
operator fun Pair<Double, Double>.plus(other: Pair<Double, Double>): Pair<Double, Double> =
    first + other.first to second + other.second
