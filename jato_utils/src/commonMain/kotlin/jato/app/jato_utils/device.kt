package jato.app.jato_utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalWindowInfo
import kotlin.math.absoluteValue

sealed class JDevice(open val width: Int, open val height: Int) {
    data class Portrait(override val width: Int, override val height: Int) : JDevice(width, height)
    data class Landscape(override val width: Int, override val height: Int) : JDevice(width, height)
    data class Tablet(override val width: Int, override val height: Int) : JDevice(width, height)
}



@Composable
fun rememberJDevice(): JDevice {
    val configuration = LocalWindowInfo.current
    val check = (configuration.containerSize.width-configuration.containerSize.height).absoluteValue
    return remember(check) {
        when {
            configuration.containerSize.width.isBetween(600, 700) -> {
                JDevice.Tablet(
                    configuration.containerSize.width,
                    configuration.containerSize.height
                )
            }

            configuration.containerSize.width > configuration.containerSize.height -> {
                JDevice.Landscape(
                    configuration.containerSize.width,
                    configuration.containerSize.height
                )
            }

            else -> {
                JDevice.Portrait(
                    configuration.containerSize.width,
                    configuration.containerSize.height
                )
            }
        }
    }
}
