package it.unibo.collektive.vmc.costants

/**
 * Factor of adjustment.
 */
const val alpha = 0.9 //1.0

/**
 * Constant addition rate.
 */
const val beta = 0.9 // 0.2

/**
 * Constant decay rate of vessels.
 */
const val c = 1.0 // 0.25

/**
 * Transfer rate influencing the rate of reduction in the Successin flow passing a node.
 * It is independent of the sensor's input.
 * Should be a value between [0, 1)
 */
const val pConst = 0.9 // 0.5

/**
 * Resource quantity generated at root.
 */
const val Rroot = 400.0

/**
 * Threshold for the growing of new leaves.
 */
const val thAdd = 3.0

/**
 * Threshold for deleting branches.
 */
const val thDel = 3.0

/**
 * Production rate of Successin at the leaf independent of the sensor inputs.
 * Should be a value between [0, 1)
 */
const val wConst = 0.1

/**
 * Determines the weight associated with the sensor's input
 * Should be a value between [-1, 1)
 */
const val wLight = 1.0
