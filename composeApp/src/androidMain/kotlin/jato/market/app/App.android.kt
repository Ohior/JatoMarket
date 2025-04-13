package jato.market.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class AppActivity : ComponentActivity() {
    companion object{
        lateinit var instance: AppActivity
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        enableEdgeToEdge()
        setContent { App() }
    }
}

@Preview
@Composable
fun AppPreview() { App() }
