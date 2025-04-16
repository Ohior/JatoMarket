import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import jato.market.app.App

//@OptIn(ExperimentalComposeUiApi::class)
//fun main() {
//    onWasmReady {
//        val body = document.body ?: return@onWasmReady
//        ComposeViewport(body) {
//            App()
//        }
//    }
//}

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        App()
    }
}