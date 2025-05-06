package jato.market.app.screens.stores

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import jato.market.app.data_model.UserModel
import jato.market.app.database.local.JsonLocalDatabase
import jato.market.app.database.remote.JatoDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StoresScreenModel : ScreenModel {
    var searchQuery by mutableStateOf("")

    //    val jsonDatabase = JsonLocalDatabase
//        .getUserDataFlow()
//        .stateIn(screenModelScope, SharingStarted.Eagerly, UserModel.empty())
    val storeFlow = JatoDatabase
        .getAllStoresFlow()
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptyList())
    var localUser: UserModel? = null
        private set

    init {
        screenModelScope.launch {
            localUser = JsonLocalDatabase.getUserData()
        }
    }
}

