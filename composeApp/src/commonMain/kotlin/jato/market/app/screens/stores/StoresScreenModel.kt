package jato.market.app.screens.stores

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import jato.market.app.data_model.UserModel
import jato.market.app.database.local.JsonLocalDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class StoresScreenModel:ScreenModel {
    var searchQuery by mutableStateOf("")
    val jsonDatabase = JsonLocalDatabase
        .getUserDataFlow()
        .stateIn(screenModelScope, SharingStarted.Eagerly, UserModel.empty())


}