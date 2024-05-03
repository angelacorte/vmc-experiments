package it.unibo.collektive.vmc

import it.unibo.alchemist.collektive.device.DistanceSensor
import it.unibo.alchemist.model.times.DoubleTime
import it.unibo.alchemist.util.RandomGenerators.nextDouble
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.sensors.DeviceSpawn
import it.unibo.collektive.alchemist.device.sensors.EnvironmentVariables
import it.unibo.collektive.alchemist.device.sensors.LeaderSensor
import it.unibo.collektive.alchemist.device.sensors.LocationSensor
import it.unibo.collektive.alchemist.device.sensors.RandomSuccess
import it.unibo.collektive.alchemist.device.sensors.ResourceSensor
import it.unibo.collektive.alchemist.device.sensors.SelfDestroy
import it.unibo.collektive.alchemist.device.sensors.SuccessSensor
import it.unibo.collektive.coordination.findParent
import it.unibo.collektive.field.Field.Companion.hood
import it.unibo.collektive.lib.chooseLeader
import it.unibo.collektive.lib.convergeSuccess
import it.unibo.collektive.lib.findPotential
import it.unibo.collektive.lib.metrics.MyHopMetric
import it.unibo.collektive.lib.obtainLocalSuccess
import it.unibo.collektive.lib.spreadResource
import java.util.Random
import kotlin.math.nextUp

context(
EnvironmentVariables,
DeviceSpawn,
SelfDestroy,
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
    SelfDestroy,
    LocationSensor,
    LeaderSensor,
    ResourceSensor,
    SuccessSensor,
    RandomSuccess
)
@JvmOverloads
fun Aggregate<Int>.predappio(
    resourceLowerBound: Double = 10.0,
    certainSpawnThreshold: Double = 100.0,
    maxChildren: Int = 1,
): Double = with (MyHopMetric()) {
    vmc { potential: Double, _: Double, _: Double, localResource: Double ->
        val children = neighboring(findParent(potential))
            .hood(0) { acc, parent -> acc + if (parent == localId) 1 else 0 }
        val neighbors = neighboring(coordinates())
        operator fun Pair<Double, Double>.minus(other: Pair<Double, Double>): Pair<Double, Double> =
            first - other.first to second - other.second
        operator fun Pair<Double, Double>.plus(other: Pair<Double, Double>): Pair<Double, Double> =
            first + other.first to second + other.second
        val localPosition = neighbors.localValue
        when {
            nextRandomDouble(0.0..certainSpawnThreshold) < localResource && children < maxChildren -> {
                val relativeDestination = neighbors.map { it - localPosition }
                    .hood(1.0 to 1.0) { acc, pair -> acc + pair }
                val absoluteDestination = localPosition - relativeDestination
                spawn(absoluteDestination)
            }
            nextRandomDouble(0.0..resourceLowerBound) > localResource -> destroy()
        }
    }
}

context(
    EnvironmentVariables,
    DistanceSensor,
    DeviceSpawn,
    SelfDestroy,
    LocationSensor,
    LeaderSensor,
    ResourceSensor,
    SuccessSensor
)
fun Aggregate<Int>.vmc(spawner: Spawner): Double {
    val isLeader = chooseLeader()
    val potential = findPotential(isLeader)
    val localSuccess = obtainLocalSuccess()
    val success = convergeSuccess(potential, localSuccess)
    val localResource = spreadResource(potential, success)
    spawner(this@DeviceSpawn, this@LocationSensor, this, potential, localSuccess, success, localResource)
    return localResource
}

typealias Spawner = context(DeviceSpawn, LocationSensor) Aggregate<Int>.(potential: Double, localSuccess: Double, success: Double, localResource: Double) -> Unit
