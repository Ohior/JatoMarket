package jato.market.app.screens.stores

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import jato.market.app.data_model.UserModel
import jato.market.app.database.JsonLocalDatabase
import jato.market.app.getJsonDatabase

class StoresScreenModel:ScreenModel {
    var searchQuery by mutableStateOf("")
    val jsonDatabase = JsonLocalDatabase()

}