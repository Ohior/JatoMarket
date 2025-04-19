package jato.market.app.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import jato.app.jato_utils.getIfNotNull
import jato.market.app.data_model.UserModel
import jato.market.app.database.local.JsonLocalDatabase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn

class ProfileScreenModel : ScreenModel {
    val userFlow = JsonLocalDatabase
        .getUserDataFlow()
        .stateIn(screenModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, null)

    var user = userFlow.value
        private set
    var readOnly by mutableStateOf(true)
    var createStorePopup by mutableStateOf(false)
    var firstName by mutableStateOf(user.getIfNotNull("") { it.firstName })
    var lastName by mutableStateOf(user.getIfNotNull("") { it.lastName })
    var email by mutableStateOf(user.getIfNotNull("") { it.email })
    var imageUrl by mutableStateOf(user?.imageUrl)

    val store = user?.store
    var storeName by mutableStateOf(store.getIfNotNull("") { it.storeName })
    var storeDescription by mutableStateOf(store.getIfNotNull("") { it.storeDescription })
    var storeImageUrl by mutableStateOf(store.getIfNotNull("") { it.imageUrl })
    val products = store?.products

    private fun updateUserData(userModel: UserModel) {
        user = userModel
    }

    fun saveButtonClicked() {
        // save and update user in database
    }

    fun createStore(user: UserModel) {
        // create store and update user in database
    }
}