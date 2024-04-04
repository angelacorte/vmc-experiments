package it.unibo.collektive.lib

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.aggregate.api.operators.share
import it.unibo.collektive.field.Field.Companion.hood

/**
 * Aggregate a field of type T within a spanning tree built according to the maximum
 * decrease in potential. Accumulate the [potential] according to the [reduce] function.
 *
 * @param potential num, gradient of which gives aggregation direction
 * @param reduce    (T, T) -> T, function
 * @param local     T, local value
 * @return          T, aggregated value
 */
fun <T, ID: Any> Aggregate<ID>.convergeCast(
    potential: Double,
    reduce: (T, T) -> T,
    local: T,
    emptyField: T,
): T = share(local) { field ->
        reduce(
            local,
            field.hood(local) { _, _ ->
                if (neighboring(getParent(potential)).localId == this.localId) {
                    field.localValue
                } else {
                    emptyField
                }
            }
        )
    }

fun <ID: Any> Aggregate<ID>.getParent(potential: Double): ID {
    val (minPotential, potentialID) = neighboring(potential to this.localId).localValue
    return if (minPotential < potential) {
        potentialID
    } else {
        this.localId
    }
}
