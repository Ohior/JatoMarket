package jato.app.jato_utils

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
inline fun <T : Any> T?.getIfNotNull(default: T, action: (T) -> T): T {
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