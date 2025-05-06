package jato.app.jato_utils

import kotlin.time.Clock.System
import kotlin.time.ExperimentalTime

object JUtils {
    @OptIn(ExperimentalTime::class)
    fun generateUniqueId(maxTries: Int = 8): String {
        val timestamp = System.now().toEpochMilliseconds()
        val randomPart = (1..maxTries)
            .map { ('A'..'Z') + ('0'..'9') }
            .flatten()
            .shuffled()
            .take(6)
            .joinToString("")
        return "$timestamp-$randomPart"
    }
}