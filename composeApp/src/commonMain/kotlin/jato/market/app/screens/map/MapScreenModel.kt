package jato.market.app.screens.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import jato.market.app.data_model.StoreModel
import jato.market.app.data_model.UserModel
import jato.market.app.database.remote.JatoDatabase
import kotlinx.coroutines.launch

class MapScreenModel(userDocumentId: String) : ScreenModel {
    var user by mutableStateOf<UserModel?>(null)
        private set
    var store by mutableStateOf<StoreModel?>(null)
        private set
    init {
        screenModelScope.launch {
            user = JatoDatabase.getUserData(userDocumentId)
            store = user?.storeDocumentId?.let { JatoDatabase.getStoreData(it) }
        }
    }

}