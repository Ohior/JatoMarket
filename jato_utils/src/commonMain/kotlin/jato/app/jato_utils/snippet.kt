package jato.app.jato_utils

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt

fun Number.isBetween(start: Number, end: Number): Boolean {
    return when (this) {
        is Float -> {
            this >= start.toFloat() && this <= end.toFloat()
        }

        is Double -> {
            this >= start.toDouble() && this <= end.toDouble()
        }

        is Int -> {
            this >= start.toInt() && this <= end.toInt()
        }

        else -> throw NumberFormatException("Unsupported Number type for comparison")
    }
}

/**
 * Executes [action] with the non-null value if [value] is not null and returns a null value
 */
inline fun <A : Any, B> A?.getIfNotNull(default: B, action: (A) -> B): B {
    return if (this != null) action(this)
    else default
}

/**
 * Executes [action] with the non-null value if [value] is not null
 */
inline fun <T : Any> T?.ifNotNull(action: (T) -> Unit): T? {
    if (this != null) action(this)
    return this
}

/**
 * Executes [action] if [value] is null
 */
inline fun <T : Any> T?.ifNull(action: () -> Unit): T? {
    if (this == null) action()
    return this
}

/**
 * Calculates the ratio of two parts relative to a whole number.
 *
 * This function divides the whole number based on the ratio defined by `part1` and `part2`.
 * It ensures that both parts are not zero to avoid division by zero and
 * uses integer arithmetic for efficiency when possible.
 *
 * @param part1 The first part of the ratio.
 * @param part2 The second part of the ratio.
 * @return A [Pair] where the first element is the calculated value for `part1`,
 *         and the second element is the calculated value for `part2`.
 * @throws IllegalArgumentException if both `part1` and `part2` are zero.
 */
fun Int.calculateRatio(
    part1: Int,
    part2: Int
): Pair<Int, Int> {
    val sum = part1 + part2
    if (sum == 0) throw IllegalArgumentException("Parts must not both be zero") // Check for division by zero
    val ratio1 = (part1 * this) / sum // using int operation and avoiding double calculation
    val ratio2 = this - ratio1 // optimizing calculations


    return Pair(ratio1, ratio2)
}

/**
 * Converts a value from degrees to radians using the mathematical formula.
 *
 * @param degrees The angle value in degrees.
 * @return The angle value converted to radians.
 */
fun Double.toRadians(): Double {
    // The formula is degrees * (PI / 180)
    return this * (PI / 180.0)
}

/**
 * Converts a value from radians to degrees using the mathematical formula.
 *
 * @param radians The angle value in radians.
 * @return The angle value converted to degrees.
 */
fun Double.toDegrees(): Double {
    // The formula is radians * (180 / PI)
    return this * (180.0 / PI)
}

/**
 * Rounds a Double value to a specified number of decimal places.
 *
 * @param number The Double value to round.
 * @param decimalPlaces The number of decimal places to round to.
 * @return The rounded Double value.
 */
fun Double.roundToDecimalPlaces(decimalPlaces: Int): Double {
    val factor = 10.0.pow(decimalPlaces)
    return round(this * factor) / factor
}


/**
 * Calculates the distance between two points on the Earth's surface
 * using the Haversine formula.
 *
 * @param lat1 Latitude of the first point in degrees.
 * @param lon1 Longitude of the first point in degrees.
 * @param lat2 Latitude of the second point in degrees.
 * @param lon2 Longitude of the second point in degrees.
 * @return The distance between the two points in kilometers.
 */
fun calculateDistanceKM(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    // Radius of the Earth in kilometers
    val earthRadius = 6371.0

    // Convert degrees to radians
    val lat1Rad = lat1.toRadians()
    val lon1Rad = lon1.toRadians()
    val lat2Rad = lat2.toRadians()
    val lon2Rad = lon2.toRadians()

    // Difference in coordinates
    val dLat = lat2Rad - lat1Rad
    val dLon = lon2Rad - lon1Rad

    // Haversine formula calculation
    val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    // Distance in kilometers
    val distance = earthRadius * c

    return distance
}
