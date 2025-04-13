package jato.market.app.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import jato.market.app.data_model.UserModel

class ProfileScreen(private val userModel: UserModel? = null) : Screen {
    @Composable
    override fun Content() {
        Scaffold(Modifier.fillMaxSize()) { pv ->
            Column(Modifier.padding(pv).fillMaxSize()) {
                Text("Profile Screen")
            }
        }
    }
}