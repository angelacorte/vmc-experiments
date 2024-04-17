package it.unibo.collektive.lib

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.aggregate.api.operators.share
import it.unibo.collektive.field.Field.Companion.fold
import it.unibo.collektive.field.Field.Companion.hood
import it.unibo.collektive.field.min

/**
 * Aggregate a field of type T within a spanning tree built according to the maximum
 * decrease in [potential].
 * Accumulate the [potential] according to the [reduce] function.
 * [local] is the value field providing the value to be collected for each device.
 */
fun <T, ID: Comparable<ID>> Aggregate<ID>.convergeCast(
    potential: Double,
    local: T,
    disambiguateParent: (ID, ID) -> ID = { a, b -> minOf(a, b) },
    reduce: (T, T) -> T,
): T = share(local) { field ->
    val neighboringPotential = neighboring(potential)
    val localMinimum = neighboringPotential.min(potential)
    val parent: ID = neighboringPotential.asSequence()
        .filter { (_, v) -> v == localMinimum }
        .map { it.first }
        .reduce(disambiguateParent)
    val neighborParents = neighboring(parent) // Each device is mapped to its parent
    // A field mapping input channels to this device to the value channelled in
    data class Channel(val isFromChild: Boolean, val localValue: T)
    val childrenValues = neighborParents.alignedMap(field) { itsParent, itsLocal ->
        Channel(isFromChild = itsParent == localId, itsLocal)
    }
    childrenValues.fold(local) { accumulator, channel ->
        if (channel.isFromChild) reduce(accumulator, channel.localValue) else accumulator
    }
}
