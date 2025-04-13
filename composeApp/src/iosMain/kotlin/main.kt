import androidx.compose.ui.window.ComposeUIViewController
import jato.market.app.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
