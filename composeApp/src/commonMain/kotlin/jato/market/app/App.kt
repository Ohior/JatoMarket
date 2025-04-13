package jato.market.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import jato.market.app.screens.home.HomeScreen
import jato.market.app.theme.AppTheme

@Composable
internal fun App() = AppTheme {
    Navigator(HomeScreen)
}
