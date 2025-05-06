package jato.market.app.utils

import jato.app.jato_utils.toDegrees
import jato.app.jato_utils.toRadians
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

object Tools {
    fun getAngle(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {

        val rad1 = lat1.toRadians()
        val rad2 = lat2.toRadians()
        val rad3 = (lon2 - lon1).toRadians()

        val y = sin(rad3) * sin(rad2)
        val x = cos(rad1) * sin(rad2) - sin(rad1) * cos(rad2) * cos(rad3)
        var degree = atan2(y, x).toDegrees()

        degree = (degree + 360) % 360  // Normalize to 0–360 degrees
        return degree
    }
}